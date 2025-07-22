package gift.product.service;

import gift.product.dto.CreateOptionRequest;
import gift.product.dto.OptionResponseDto;

import java.util.List;

public interface OptionService {
    List<OptionResponseDto> getOptionsByProductId(Long productId);

    OptionResponseDto addOptionToProduct(Long productId, CreateOptionRequest createOptionRequest);
}
