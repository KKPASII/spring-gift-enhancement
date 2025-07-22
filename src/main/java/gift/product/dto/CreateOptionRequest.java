package gift.product.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public record CreateOptionRequest(
    @NotBlank(message = "옵션 이름을 반드시 입력해야 합니다.")
    @Pattern(
        regexp = "^(?=.*[a-zA-Z가-힣])[a-zA-Z0-9가-힣()[\\\\]+\\-&/_ ]{1,50}$",
        message = "옵션 이름은 공백과 특수문자((), [], +, -, &, /, _)를 포함하여 50자 이내로 작성할 수 있습니다."
    )
    @Length(min=1, max=50)
    String name,

    @NotNull
    @Min(value = 1, message = "상품 옵션은 1개 이상이어야 합니다.")
    @Max(value = 99_999_999, message = "상품 옵션은 99,999,999개를 초과할 수 없습니다.")
    int quantity
) {
}
