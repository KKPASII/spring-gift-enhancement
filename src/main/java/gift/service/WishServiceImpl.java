package gift.service;

import gift.dto.CreateWishRequest;
import gift.dto.CreateWishResponse;
import gift.dto.ProductResponseDto;
import gift.dto.WishResponse;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class WishServiceImpl implements WishService {
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishServiceImpl(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
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
            .map(wish -> new WishResponse(
                wish.getId(),
                new ProductResponseDto(
                    wish.getProduct().getId(),
                    wish.getProduct().getName(),
                    wish.getProduct().getPrice(),
                    wish.getProduct().getImageUrl()),
                wish.getQuantity())
            )
            .toList();
    }

    @Override
    @Transactional
    public void deleteWish(Long wishId, Long memberId) {
        wishRepository.deleteByIdAndMemberId(wishId, memberId);
    }
}
