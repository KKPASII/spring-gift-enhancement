package gift.validator;

import gift.exception.InvalidProductNameException;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {
    public void validateProductName(String name) {
        if (name.contains("카카오")) {
            throw new InvalidProductNameException("'카카오'가 포함된 문구는 담당 MD와 협의가 필요합니다.");
        }
    }
}
