package gift.service;

import gift.dto.AuthRequest;
import gift.dto.AuthToken;
import gift.entity.Member;

import java.util.Optional;

public interface MemberService {
    AuthToken register(AuthRequest request);

    AuthToken login(AuthRequest request);

    Optional<Member> findByEmail(String email);
}