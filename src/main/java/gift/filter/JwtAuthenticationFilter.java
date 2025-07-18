package gift.filter;

import gift.entity.Role;
import gift.exception.UnAuthenticatedException;
import gift.exception.UnAuthorizedException;
import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

import static gift.config.AuthConstants.BEARER_PREFIX;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;

        // 헤더 확인
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            token = authHeader.substring(BEARER_PREFIX.length());
        }

        // 쿠키 확인
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
                        return null;
                    } catch (Exception e) {
                        throw new UnAuthenticatedException(e.getMessage());
                    }
                })
                .orElse(null);
        }

        if (token == null) {
            throw new UnAuthenticatedException("인증 헤더가 없거나 'Bearer' 타입이 아닙니다.");
        }

        Claims claims = jwtUtil.getClaims(token);
        String role = claims.get("role", String.class);
        if (role == null || !Role.valueOf(role).equals(Role.ADMIN)) {
            throw new UnAuthorizedException("해당 리소스에 접근할 권한이 없습니다.");
        }

        filterChain.doFilter(request, response);
    }
}
