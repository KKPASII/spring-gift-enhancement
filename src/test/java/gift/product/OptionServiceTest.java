package gift.product;

import gift.product.dto.CreateOptionRequest;
import gift.product.entity.Product;
import gift.product.repository.OptionRepository;
import gift.product.repository.ProductRepository;
import gift.product.service.OptionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Transactional
@SpringBootTest
class OptionServiceTest {

    @Autowired
    private OptionService optionService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Test
    @DisplayName("옵션 이름은 공백을 포함할 수 있다.")
    void 옵션_이름은_공백을_포함할_수_있다() {
        // given
        Product product = createProduct();
        String optionName = "test name";
        CreateOptionRequest requestDto = new CreateOptionRequest(optionName, 100);

        // when & then
        assertDoesNotThrow(() -> {
            optionService.addOptionToProduct(product.getId(), requestDto);
        });

        assertThat(product.getOptions()).hasSize(1);
        assertThat(product.getOptions().get(0).getName()).isEqualTo(optionName);
    }

    @Test
    @DisplayName("옵션 이름 길이가 50자를 넘으면 예외가 발생한다.")
    void 옵션_이름_길이가_50자를_넘으면_예외가_발생한다() {
        // given
        Product product = createProduct();
        String optionName = "a".repeat(51);
        CreateOptionRequest requestDto = new CreateOptionRequest(optionName, 100);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> optionService.addOptionToProduct(product.getId(), requestDto));
    }

    @DisplayName("옵션 수량이 1억 개 이상이면 예외가 발생한다.")
    @ParameterizedTest // 파라미터를 받아 여러 번 테스트 실행
    @ValueSource(ints = {100_000_000, 100_000_001, 200_000_000})
    void 옵션_수량이_1억_개_이상이면_예외가_발생한다(int quantity) {
        // given
        Product product = createProduct();
        CreateOptionRequest requestDto = new CreateOptionRequest("valid name", quantity);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> optionService.addOptionToProduct(product.getId(), requestDto));
    }


    @Test
    @DisplayName("동일한 상품 내의 옵션 이름이 중복되면 예외가 발생한다.")
    void 동일한_상품_내의_옵션_이름이_중복되면_예외가_발생한다() {
        // given
        Product product = createProduct();
        String duplicateName = "중복 옵션 이름";
        CreateOptionRequest requestDto = new CreateOptionRequest(duplicateName, 100);

        optionService.addOptionToProduct(product.getId(), requestDto);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> optionService.addOptionToProduct(product.getId(), requestDto));
    }

    private Product createProduct() {
        return productRepository.save(new Product("name", 1_000, "imageUrl"));
    }
}