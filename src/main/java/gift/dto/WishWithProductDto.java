package gift.dto;

public record WishWithProductDto(
    long wishId,
    int quantity,
    long productId,
    String productName,
    int productPrice,
    String productImageUrl
) {
}
