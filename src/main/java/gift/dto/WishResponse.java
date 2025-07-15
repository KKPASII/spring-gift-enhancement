package gift.dto;

public record WishResponse(
    long id,
    ProductResponseDto product,
    int quantity
) {
}
