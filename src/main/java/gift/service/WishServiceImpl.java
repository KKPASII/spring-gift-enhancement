package gift.service;

import gift.dto.CreateWishRequest;
import gift.dto.CreateWishResponse;
import gift.dto.ProductResponseDto;
import gift.dto.WishResponse;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishServiceImpl implements WishService {
    private final WishRepository wishRepository;
    private final ProductService productService;

    public WishServiceImpl(WishRepository wishRepository, ProductService productService) {
        this.wishRepository = wishRepository;
        this.productService = productService;
    }

    @Override
    public CreateWishResponse create(Long memberId, CreateWishRequest request) {
        Optional<CreateWishResponse> wish = wishRepository.findProductById(memberId, request.productId());
        if (wish.isPresent()) {
            throw new IllegalArgumentException("이미 위시 리스트에 존재하는 상품입니다.");
        }
        return wishRepository.saveWish(memberId, request.productId(), request.quantity());
    }

    @Override
    public List<WishResponse> findAllWishes(Long memberId) {
        return wishRepository.findAllWishesWithProductByMemberId(memberId)
            .stream()
            .map(wish -> new WishResponse(
                wish.wishId(),
                new ProductResponseDto(
                    wish.productId(),
                    wish.productName(),
                    wish.productPrice(),
                    wish.productImageUrl()
                ), wish.quantity())
            )
            .toList();
    }

    @Override
    public void deleteWish(Long memberId, Long wishId) {
        wishRepository.deleteWish(memberId, wishId);
    }
}
