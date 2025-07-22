package gift.service;

import gift.dto.CreateOptionRequest;
import gift.dto.OptionResponseDto;

import java.util.List;

public interface OptionService {
    List<OptionResponseDto> getOptionsByProductId(Long productId);

    OptionResponseDto addOptionToProduct(Long productId, CreateOptionRequest createOptionRequest);
}
