package gift.dto;

public record CreateWishRequest(
    long productId,
    int quantity
) {
}
