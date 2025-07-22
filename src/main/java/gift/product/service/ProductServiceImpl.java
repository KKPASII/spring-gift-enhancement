package gift.product.service;

import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.entity.Option;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.product.validator.ProductValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    public ProductServiceImpl(ProductRepository productRepository, ProductValidator productValidator) {
        this.productRepository = productRepository;
        this.productValidator = productValidator;
    }

    @Override
    @Transactional
    public ProductResponseDto saveProduct(ProductRequestDto requestDto) {
        productValidator.validateProductName(requestDto.getName());

        Product product = new Product(requestDto.getName(), requestDto.getPrice(), requestDto.getImageUrl());
        requestDto.getOptions().forEach(optionDto -> {
            product.checkDuplicatedName(optionDto.name());
            Option option = new Option(optionDto.name(), optionDto.quantity());
            product.addOption(option);
        });

        Product savedProduct = productRepository.save(product);
        return new ProductResponseDto(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,  "해당 ID의 상품이 없습니다."));
        return new ProductResponseDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findAllProducts() {
        List<Product> allProducts = productRepository.findAll();

        return allProducts.stream()
            .map(ProductResponseDto::new)
            .toList();
    }

    @Override
    @Transactional
    public void updateProduct(Long id, ProductRequestDto requestDto) {
        productValidator.validateProductName(requestDto.getName());
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,  "해당 ID의 상품이 없습니다."));
        product.update(requestDto);
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) {
        productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,  "해당 ID의 상품이 없습니다."));
        productRepository.deleteById(id);
    }

    @Override
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    @Override
    public Page<ProductResponseDto> findAll(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductResponseDto::new);
    }
}
