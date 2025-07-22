package gift.member;

import gift.member.entity.Member;
import gift.member.entity.Role;
import gift.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void SetUp() {
        member = new Member("test@example.com", "password", Role.USER);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("이메일로 회원 조회")
    void findByEmail() {
        // 이메일이 존재함
        Optional<Member> member1 = memberRepository.findByEmail("test@example.com");
        assertThat(member1).isPresent();
        assertThat(member1.get().getEmail()).isEqualTo("test@example.com");
        assertThat(member1.get().getId()).isEqualTo(member.getId());

        // 없는 이메일로 검색
        Optional<Member> member2 = memberRepository.findByEmail("nonexistent@example.com");
        assertThat(member2).isEmpty();
    }
}
