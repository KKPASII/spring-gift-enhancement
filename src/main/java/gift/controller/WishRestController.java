package gift.controller;

import gift.dto.CreateWishRequest;
import gift.dto.CreateWishResponse;
import gift.dto.WishResponse;
import gift.entity.Member;
import gift.service.WishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishRestController {
    private final WishService wishService;

    WishRestController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<List<WishResponse>> showAllWishes(@LoginMember Member member) {
        return new ResponseEntity<>(wishService.findAllWishes(member.getId()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CreateWishResponse> addWishes(
        @RequestBody CreateWishRequest request,
        @LoginMember Member member
    ) {
        return new ResponseEntity<>(wishService.create(member, request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<WishResponse> deleteOneWish(
        @PathVariable("wishId") Long wishId,
        @LoginMember Member member
    ) {
        wishService.deleteWish(wishId, member.getId());
        return ResponseEntity.noContent().build();
    }
}
