package gift.repository;

import gift.entity.Member;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository {
    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberRepositoryImpl(DataSource dataSource) {
        this.jdbcClient = JdbcClient.create(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("member")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(Member member) {
        Map<String, Object> parameters = Map.of(
            "email", member.getEmail(),
            "password", member.getPassword(),
            "role", member.getRole().name()
        );

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new Member(id, member.getEmail(), member.getPassword(), member.getRole());
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, email, password, role FROM member WHERE email = ?";

        return jdbcClient.sql(sql)
            .param(email)
            .query(Member.class)
            .optional();
    }
}
