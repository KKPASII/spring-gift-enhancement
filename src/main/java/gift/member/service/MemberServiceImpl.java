package gift.member.service;

import gift.auth.dto.AuthRequest;
import gift.auth.dto.AuthToken;
import gift.global.exception.DuplicatedEmailException;
import gift.global.exception.LoginFailedException;
import gift.member.repository.MemberRepository;
import gift.member.entity.Member;
import gift.member.entity.Role;
import gift.global.util.JwtUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberServiceImpl(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public AuthToken register(AuthRequest request) {
        if (memberRepository.findByEmail(request.email()).isPresent()) {
            throw new DuplicatedEmailException("사용할 수 없는 이메일입니다.");
        }
        Member savedMember = memberRepository.save(new Member(request.email(), request.password(), Role.USER));
        return new AuthToken(jwtUtil.generateAccessToken(savedMember));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthToken login(AuthRequest request) {
        Member member = memberRepository.findByEmail(request.email())
            .orElse(null);

        if (member == null || !request.password().equals(member.getPassword())) {
            throw new LoginFailedException("이메일 또는 비밀번호가 틀렸습니다.");
        }
        String accessToken = jwtUtil.generateAccessToken(member);
        return new AuthToken(accessToken);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}