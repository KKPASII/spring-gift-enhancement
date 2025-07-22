package gift.wish.dto;

import gift.product.entity.Product;

public record CreateWishResponse(
    long id,
    long memberId,
    Product product,
    int quantity
) {
}
