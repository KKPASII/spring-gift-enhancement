package gift.wish.service;

import gift.member.entity.Member;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.wish.dto.CreateWishRequest;
import gift.wish.dto.CreateWishResponse;
import gift.wish.dto.WishMapper;
import gift.wish.dto.WishResponse;
import gift.wish.entity.Wish;
import gift.wish.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class WishServiceImpl implements WishService {
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final WishMapper wishMapper;

    public WishServiceImpl(WishRepository wishRepository, ProductRepository productRepository, WishMapper wishMapper) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.wishMapper = wishMapper;
    }

    @Override
    @Transactional
    public CreateWishResponse create(Member member, CreateWishRequest request) {
        wishRepository.findByMemberIdAndProductId(member.getId(), request.productId())
            .ifPresent(wish -> {
                throw new IllegalArgumentException("이미 위시 리스트에 존재하는 상품입니다.");
            });

        Product product = productRepository.findById(request.productId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다."));

        Wish wish = new Wish(member, product, request.quantity());
        wishRepository.save(wish);

        return new CreateWishResponse(
            wish.getId(),
            member.getId(),
            product,
            request.quantity()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<WishResponse> findAllWishes(Long memberId) {
        return wishRepository.findAllByMemberId(memberId)
            .stream()
            .map(wishMapper::toWishResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WishResponse> findAll(Long memberId, Pageable pageable) {
        return wishRepository.findAllByMemberId(memberId, pageable)
            .map(wishMapper::toWishResponse);
    }

    @Override
    @Transactional
    public void deleteWish(Long wishId, Long memberId) {
        wishRepository.deleteByIdAndMemberId(wishId, memberId);
    }
}
