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
        String jwt = null;
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            authHeader = authHeader.trim();
        }

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            jwt = authHeader.substring(BEARER_PREFIX.length());
        }

        if (authHeader == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                throw new UnAuthenticatedException("인증 정보가 없습니다. (쿠키 없음)");
            }

            Optional<String> accessTokenCookie = Arrays.stream(cookies)
                .filter(cookie -> "accessToken".equals(cookie.getName()))
                .map(cookie -> URLDecoder.decode(cookie.getValue()))
                .findFirst();

            if (accessTokenCookie.isEmpty()) {
                throw new UnAuthenticatedException("인증 정보가 없습니다. (토큰 쿠키 없음)");
            }
            jwt = accessTokenCookie.get().substring(BEARER_PREFIX.length());
        }

        if (jwt == null) {
            throw new UnAuthenticatedException("인증 헤더가 없거나 'Bearer' 타입이 아닙니다.");
        }

        Claims claims = jwtUtil.getClaims(jwt);
        String roleString = claims.get("role", String.class);
        if (roleString == null || !Role.valueOf(roleString).equals(Role.ADMIN)) {
            throw new UnAuthorizedException("해당 리소스에 접근할 권한이 없습니다.");
        }

        filterChain.doFilter(request, response);
    }
}
