package gift.wish.dto;

import gift.product.dto.ProductResponseDto;

public record WishResponse(
    long id,
    ProductResponseDto product,
    int quantity
) {
}
