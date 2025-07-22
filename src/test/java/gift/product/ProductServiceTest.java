package gift.product;

import gift.product.dto.CreateOptionRequest;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.repository.ProductRepository;
import gift.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 생성 시 옵션들이 저장된다")
    void saveProduct() {
        ProductRequestDto requestDto = createRequestDto();

        var response = productService.saveProduct(requestDto);

        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(ProductResponseDto.class);
        assertThat(response.getName()).isEqualTo("계란");
        assertThat(response.getPrice()).isEqualTo(5000);
        assertThat(response.getImageUrl()).isEqualTo("https://image.jpg");

        var savedProduct = productRepository.findById(response.getId()).get();

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getOptions().size()).isEqualTo(2);
        assertThat(savedProduct.getOptions().get(0).getName()).isEqualTo("중란");
        assertThat(savedProduct.getOptions().get(0).getQuantity()).isEqualTo(100);
        assertThat(savedProduct.getOptions().get(1).getName()).isEqualTo("대란");
        assertThat(savedProduct.getOptions().get(1).getQuantity()).isEqualTo(200);
    }

    private ProductRequestDto createRequestDto() {
        var optionDto1 = new CreateOptionRequest("중란", 100);
        var optionDto2 = new CreateOptionRequest("대란", 200);
        return new ProductRequestDto(
            "계란",
            5000,
            "https://image.jpg",
            List.of(optionDto1, optionDto2)
        );
    }

    @Test
    @DisplayName("상품 생성 시 초기 옵션에 중복된 이름이 있으면 예외가 발생한다")
    void saveProductWithDuplicatedName() {
        var option1 = new CreateOptionRequest("중란", 100);
        var option2 = new CreateOptionRequest("중란", 200);

        var requestDto = new ProductRequestDto(
            "계란",
            5000,
            "https://image.jpg",
            List.of(option1, option2)
        );

        assertThatIllegalArgumentException().isThrownBy(() -> productService.saveProduct(requestDto));
    }
}
