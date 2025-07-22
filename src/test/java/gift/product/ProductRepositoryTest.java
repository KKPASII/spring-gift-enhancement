package gift.product;

import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    void save() {
        // given
        var product = new Product("eggs", 3990, "https://cdn.pixabay.com/photo/2018/07/23/18/43/hens-egg-3557544_1280.jpg");

        // when
        var actual = productRepository.save(product);

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("eggs");
    }

    @Test
    void findById() {
        var actual = productRepository.save(new Product("eggs", 3990, "https://cdn.pixabay.com/photo/2018/07/23/18/43/hens-egg-3557544_1280.jpg"));
        var id = actual.getId();

        Optional<Product> product = productRepository.findById(id);
        assertThat(product).isPresent();                               // 존재함
        assertThat(product.get().getId()).isEqualTo(id);               // product id
        assertThat(product.get().getName()).isEqualTo("eggs"); // product name
        assertThat(product.get().getPrice()).isEqualTo(3990);  // product price
        assertThat(product.get().getImageUrl()).isEqualTo("https://cdn.pixabay.com/photo/2018/07/23/18/43/hens-egg-3557544_1280.jpg"); // product ImageUrl
    }
}