package gift.global.util;

import gift.member.entity.Member;
import gift.global.exception.InvalidTokenException;
import gift.global.exception.TokenExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static gift.global.config.AuthConstants.BEARER_PREFIX;

@Component
public class JwtUtil {
    private final SecretKey key;
    private final Long validityInMilliseconds;

    public JwtUtil(
        @Value("${jwt.secret}") String secretKey,
        @Value("${jwt.expiration_ms}") Long validityInMilliseconds
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String generateAccessToken(Member member) {
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + validityInMilliseconds / 2);

        return Jwts.builder()
            .subject(member.getId().toString())
            .id(UUID.randomUUID().toString())
            .claim("email", member.getEmail())
            .claim("role", member.getRole().name())
            .issuedAt(now)
            .expiration(expirationTime)
            .signWith(key, Jwts.SIG.HS512)
            .compact();
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("토큰이 만료되었습니다.");
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        }
    }

    public String extractToken(HttpServletRequest request) {
        String token = null;

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        // 예외를 던지지 않고 유효한 인증 헤더이면 토큰을 substring
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            token = authHeader.substring(BEARER_PREFIX.length());
        }
        // 유효한 헤더가 아닐 경우 token은 아직 null이며, 쿠키를 확인한다
        if (token == null && request.getCookies() != null) {
            token = Arrays.stream(request.getCookies())
                .filter(cookie -> "accessToken".equals(cookie.getName()))
                .findFirst()
                .map(cookie -> { // 쿠키에서 토큰 추출
                    try {
                        String decodedValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                        if (decodedValue.startsWith(BEARER_PREFIX)) {
                            return decodedValue.substring(BEARER_PREFIX.length());
                        }
                        return null; // Bearer 타입이 아님
                    } catch (Exception e) {
                        return null;
                    }
                })
                .orElse(null);
        }

        return token;
    }
}
