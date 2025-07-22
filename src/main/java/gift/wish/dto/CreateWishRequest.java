package gift.wish.dto;

public record CreateWishRequest(
    long productId,
    int quantity
) {
}
