package gift.auth.controller;

import gift.auth.dto.AuthRequest;
import gift.auth.dto.AuthToken;
import gift.member.entity.Role;
import gift.member.service.MemberService;
import gift.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static gift.global.config.AuthConstants.BEARER_PREFIX;

@Controller
public class LoginController {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public LoginController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> processLogin(@Valid @RequestBody AuthRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        AuthToken token = memberService.login(request);
        String accessToken = token.accessToken();

        Claims claims = jwtUtil.getClaims(accessToken);
        Role role = Role.valueOf(claims.get("role", String.class));

        Cookie cookie = new Cookie("accessToken", URLEncoder.encode(BEARER_PREFIX + accessToken, StandardCharsets.UTF_8));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 30);
        response.addCookie(cookie);

        String redirectUrl = "/";
        if (role == Role.ADMIN) {
            redirectUrl = "/management/products";
        } else if (role == Role.USER) {
            redirectUrl = "/wishes";
        }
        return ResponseEntity.ok(Map.of("redirectUrl", redirectUrl));
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }
}