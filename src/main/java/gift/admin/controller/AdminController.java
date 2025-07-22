package gift.admin.controller;

import gift.product.dto.ProductResponseDto;
import gift.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/management/products")
public class AdminController {
    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String showProductManagementPage(Model model, @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductResponseDto> products = productService.findAll(pageable);
        model.addAttribute("products", products);

        String currentSort = pageable.getSort().stream().map(order -> order.getProperty() + "," + order.getDirection().name().toLowerCase()).findFirst().orElse("id,desc");

        model.addAttribute("currentSort", currentSort);
        model.addAttribute("currentSize", pageable.getPageSize());

        return "admin/product/list";
    }

    @GetMapping("/new")
    public String showAddProductForm() {
        return "admin/product/add";
    }

    @GetMapping("/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, Model model) {
        ProductResponseDto product = productService.findProductById(id);
        model.addAttribute("product", product);
        return "admin/product/edit";
    }
}