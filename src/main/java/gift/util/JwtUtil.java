package gift.util;

import gift.entity.Member;
import gift.exception.InvalidTokenException;
import gift.exception.TokenExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

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
        Date expirationTime = new Date(now.getTime() + validityInMilliseconds/2);

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
}
