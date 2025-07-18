package gift.dto;

import gift.entity.Product;

public record CreateWishResponse(
    long id,
    long memberId,
    Product product,
    int quantity
) {
}
