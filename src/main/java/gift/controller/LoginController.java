package gift.controller;

import gift.dto.AuthRequest;
import gift.dto.AuthToken;
import gift.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URLEncoder;

import static gift.config.AuthConstants.BEARER_PREFIX;

@Controller
public class LoginController {
    private final MemberService memberService;

    public LoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @RequestBody AuthRequest request, HttpServletResponse response) {
        AuthToken token = memberService.login(request);
        Cookie cookie = new Cookie("accessToken", URLEncoder.encode(BEARER_PREFIX + token.accessToken()));
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 30);
        response.addCookie(cookie);
        return "redirect:/management/products";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }
}