package gift.member.service;

import gift.auth.dto.AuthRequest;
import gift.auth.dto.AuthToken;
import gift.member.entity.Member;

import java.util.Optional;

public interface MemberService {
    AuthToken register(AuthRequest request);

    AuthToken login(AuthRequest request);

    Optional<Member> findByEmail(String email);
}