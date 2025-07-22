package gift.global.filter;

import gift.member.entity.Role;
import gift.global.exception.UnAuthenticatedException;
import gift.global.exception.UnAuthorizedException;
import gift.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.extractToken(request);;

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
