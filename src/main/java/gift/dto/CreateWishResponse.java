package gift.dto;

public record CreateWishResponse(
    long id,
    long memberId,
    long productId,
    int quantity
) {
}
