package gift.member.controller;

import gift.auth.dto.AuthRequest;
import gift.auth.dto.AuthToken;
import gift.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthToken> register(@Valid @RequestBody AuthRequest request) {
        AuthToken token = memberService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthToken> login(@Valid @RequestBody AuthRequest request) {
        AuthToken token = memberService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}