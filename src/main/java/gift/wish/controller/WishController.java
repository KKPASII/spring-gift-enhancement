package gift.wish.controller;

import gift.auth.controller.LoginMember;
import gift.member.entity.Member;
import gift.wish.dto.WishResponse;
import gift.wish.service.WishService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wishes")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public String showWishListPage(
        @LoginMember Member member,
        Model model,
        @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC)
        Pageable pageable
    ) {
        Page<WishResponse> wishPage = wishService.findAll(member.getId(), pageable);
        model.addAttribute("wishes", wishPage);

        String currentSort = pageable.getSort().stream()
            .map(order -> order.getProperty() + "," + order.getDirection().name().toLowerCase())
            .findFirst()
            .orElse("id,desc");

        model.addAttribute("currentSort", currentSort);
        model.addAttribute("currentSize", pageable.getPageSize());

        return "user/wish/list";
    }
}
