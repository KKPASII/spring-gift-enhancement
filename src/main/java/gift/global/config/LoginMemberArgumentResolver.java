package gift.global.config;

import gift.auth.controller.LoginMember;
import gift.global.exception.InvalidTokenException;
import gift.global.exception.UnAuthenticatedException;
import gift.global.util.JwtUtil;
import gift.member.service.MemberService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

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

        String token = jwtUtil.extractToken(request);
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
