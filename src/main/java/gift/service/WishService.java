package gift.service;

import gift.dto.CreateWishRequest;
import gift.dto.CreateWishResponse;
import gift.dto.WishResponse;

import java.util.List;

public interface WishService {
    CreateWishResponse create(Long memberId, CreateWishRequest request);

    List<WishResponse> findAllWishes(Long memberId);

    void deleteWish(Long memberId, Long wishId);
}
