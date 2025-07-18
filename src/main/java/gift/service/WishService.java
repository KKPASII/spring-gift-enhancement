package gift.service;

import gift.dto.CreateWishRequest;
import gift.dto.CreateWishResponse;
import gift.dto.WishResponse;
import gift.entity.Member;

import java.util.List;

public interface WishService {
    CreateWishResponse create(Member member, CreateWishRequest request);

    List<WishResponse> findAllWishes(Long memberId);

    void deleteWish(Long wishId, Long memberId);
}
