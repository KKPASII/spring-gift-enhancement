package gift.util;

import gift.config.AuthConstants;
import gift.controller.LoginMember;
import gift.exception.InvalidTokenException;
import gift.exception.UnAuthenticatedException;
import gift.service.MemberService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static gift.config.AuthConstants.BEARER_PREFIX;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public LoginMemberArgumentResolver(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new IllegalStateException("HttpServletRequest를 가져올 수 없습니다.");
        }

        String token = null;

        String authHeader = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
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

        // 헤더 검사 & 쿠키 검사 후 토큰을 찾지 못할 경우 예외 발생
        if (token == null) {
            throw new UnAuthenticatedException("인증 토큰이 존재하지 않습니다.");
        }

        Claims claims = jwtUtil.getClaims(token);
        String email = claims.get("email", String.class);

        return memberService.findByEmail(email)
            .orElseThrow(() -> new InvalidTokenException("토큰에 해당하는 사용자를 찾을 수 없습니다."));
    }
}
