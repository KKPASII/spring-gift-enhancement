package gift.dto;

import gift.entity.Product;
import gift.entity.Wish;
import org.springframework.stereotype.Component;

@Component
public class WishMapper {
    public WishResponse toWishResponse(Wish wish) {
        if (wish == null) {
            return null;
        }

        return new WishResponse(
            wish.getId(),
            toProductDto(wish.getProduct()),
            wish.getQuantity()
        );
    }

    private ProductResponseDto toProductDto(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductResponseDto(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl()
        );
    }
}
