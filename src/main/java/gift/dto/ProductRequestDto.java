package gift.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public class ProductRequestDto {
    @NotBlank(message = "상품 이름을 반드시 입력해야 합니다.")
    @Pattern(
        regexp = "^(?=.*[a-zA-Z가-힣])[a-zA-Z0-9가-힣()[\\\\]+\\-&/_ ]{1,15}$",
        message = "상품 이름은 한글, 영문, 숫자, 특수문자((), [], +, -, &, /, _)만 사용하여 15자 이내로 작성이 가능하며, 반드시 한글 또는 영문자가 1개 이상 포함되어야 합니다."
    )
    @Length(min=1, max=15)
    private String name;

    @NotNull(message = "상품 가격을 반드시 입력해야 합니다.")
    @Min(value = 0, message = "상품 가격은 0원 이상이어야 합니다.")
    @Max(value = 1_000_000_000, message = "상품 가격은 1,000,000,000원을 초과할 수 없습니다.")
    private int price;

    @NotBlank(message = "상품 이미지 URL을 반드시 입력해야 합니다.")
    @URL(message = "유효한 URL 형식이 아닙니다.")
    @Length(min=1, max=1024)
    private String imageUrl;

    public ProductRequestDto() {
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }
}
