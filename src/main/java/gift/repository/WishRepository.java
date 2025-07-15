package gift.repository;

import gift.dto.CreateWishResponse;
import gift.dto.WishWithProductDto;

import java.util.List;
import java.util.Optional;

public interface WishRepository {
    CreateWishResponse saveWish(Long memberId, Long productId, int quantity);

    Optional<CreateWishResponse> findProductById(Long memberId, Long productId);

    List<WishWithProductDto> findAllWishesWithProductByMemberId(Long memberId);

    void deleteWish(Long memberId, Long wishId);
}
