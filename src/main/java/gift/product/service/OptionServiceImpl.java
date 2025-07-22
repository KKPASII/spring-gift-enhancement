package gift.product.service;

import gift.product.dto.CreateOptionRequest;
import gift.product.dto.OptionResponseDto;
import gift.product.entity.Option;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionServiceImpl implements OptionService {
    private final ProductRepository productRepository;

    public OptionServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OptionResponseDto> getOptionsByProductId(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품이 없습니다: " + productId));

        return product.getOptions().stream()
            .map(option -> new OptionResponseDto(option.getId(), option.getName(), option.getQuantity()))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OptionResponseDto addOptionToProduct(Long productId, CreateOptionRequest createOptionRequest) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품이 없습니다: " + productId));

        product.checkDuplicatedName(createOptionRequest.name());
        Option newOption = new Option(createOptionRequest.name(), createOptionRequest.quantity());
        product.addOption(newOption);

        return new OptionResponseDto(newOption.getId(), newOption.getName(), newOption.getQuantity());
    }
}
