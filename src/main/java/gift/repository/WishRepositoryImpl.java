package gift.repository;

import gift.dto.CreateWishResponse;
import gift.dto.WishWithProductDto;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class WishRepositoryImpl implements WishRepository {
    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public WishRepositoryImpl(DataSource dataSource) {
        this.jdbcClient = JdbcClient.create(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("wish")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public CreateWishResponse saveWish(Long memberId, Long productId, int quantity) {
        Map<String, Object> parameters = Map.of(
            "member_id", memberId,
            "product_id", productId,
            "quantity", quantity
        );

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new CreateWishResponse(id, memberId, productId, quantity);
    }

    @Override
    public Optional<CreateWishResponse> findProductById(Long memberId, Long productId) {
        String sql = "SELECT id, member_id, product_id, quantity FROM wish Where member_id = ? AND product_id = ?";

        return jdbcClient.sql(sql)
            .param(memberId)
            .param(productId)
            .query(CreateWishResponse.class)
            .optional();
    }

    @Override
    public List<WishWithProductDto> findAllWishesWithProductByMemberId(Long memberId) {
        String sql = """
                        SELECT w.id wishId, w.quantity quantity, p.id product_id, p.name productName, p.price productPrice, p.image_url productImageUrl
                        FROM wish w JOIN product p ON w.product_id = p.id
                        WHERE w.member_id = ?
                    """;
        return jdbcClient.sql(sql)
            .param(memberId)
            .query(WishWithProductDto.class)
            .list();
    }

    @Override
    public void deleteWish(Long memberId, Long wishId) {
        String sql = "DELETE FROM wish WHERE member_id = ? AND id = ?";

        jdbcClient.sql(sql)
            .param(memberId)
            .param(wishId)
            .update();
    }
}
