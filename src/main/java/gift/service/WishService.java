package gift.service;

import gift.dto.CreateWishRequest;
import gift.dto.CreateWishResponse;
import gift.dto.WishResponse;
import gift.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WishService {
    CreateWishResponse create(Member member, CreateWishRequest request);

    List<WishResponse> findAllWishes(Long memberId);

    Page<WishResponse> findAll(Long memberId, Pageable pageable);

    void deleteWish(Long wishId, Long memberId);
}
