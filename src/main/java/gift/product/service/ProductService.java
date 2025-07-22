package gift.product.service;

import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponseDto saveProduct(ProductRequestDto productRequestDto);

    ProductResponseDto findProductById(Long id);

    List<ProductResponseDto> findAllProducts();

    void updateProduct(Long productId, ProductRequestDto requestDto);

    void deleteProductById(Long id);

    void deleteAllProducts();

    Page<ProductResponseDto> findAll(Pageable pageable);
}
