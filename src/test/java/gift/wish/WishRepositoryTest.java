package gift.wish;

import gift.member.entity.Member;
import gift.product.entity.Product;
import gift.member.entity.Role;
import gift.member.repository.MemberRepository;
import gift.product.repository.ProductRepository;
import gift.wish.entity.Wish;
import gift.wish.repository.WishRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class WishRepositoryTest {
    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void findByMemberIdAndProductId() {
        var member1 = memberRepository.save(new Member("sam@email.com", "$2a$10$ISTjRWOs8SFhtJzqvE9u5Oh3TwvQxnxsI8RmKFNeSlNZOxOKCLhx.", Role.USER));
        var member2 = memberRepository.save(new Member("dam@email.com", "$2a$10$ISTjRWOs8SFhtJzqvE9u5Oh3TwvQxnxsI8RmKFNeSlNZOxOKCLhx.", Role.USER));

        var product1 = productRepository.save(new Product("eggs", 3990, "https://cdn.pixabay.com/photo/2018/07/23/18/43/hens-egg-3557544_1280.jpg"));
        var product2 = productRepository.save(new Product("hamburger", 5000, "https://cdn.pixabay.com/photo/2018/07/23/18/43/hens-egg-3557544_1280.jpg"));

        wishRepository.save(new Wish(member1, product1, 1));
        wishRepository.save(new Wish(member1, product2, 1));

        var wish1 = wishRepository.findByMemberIdAndProductId(member1.getId(), product1.getId());
        var wish2 = wishRepository.findByMemberIdAndProductId(member1.getId(), product2.getId());

        // wish1
        assertThat(wish1).isPresent();                                            // 존재함
        assertThat(wish1.get().getMember().getId()).isEqualTo(member1.getId());   // member1의 id
        assertThat(wish1.get().getProduct().getId()).isEqualTo(product1.getId()); // product1의 id
        assertThat(wish1.get().getQuantity()).isEqualTo(1);               // wish1의 quantity

        // wish2
        assertThat(wish2).isPresent();                                            // 존재함
        assertThat(wish2.get().getMember().getId()).isEqualTo(member1.getId());   // member1의 id
        assertThat(wish2.get().getProduct().getId()).isEqualTo(product2.getId()); // product2의 id
        assertThat(wish2.get().getQuantity()).isEqualTo(1);               // wish2의 quantity

        // member2의 wish가 없음
        var wish3 = wishRepository.findByMemberIdAndProductId(2L, 1L); // member2 id, product1 id
        assertThat(wish3).isEmpty();
    }

    @Test
    void findAllByMemberId() {
        var member1 = memberRepository.save(new Member("sam@email.com", "$2a$10$ISTjRWOs8SFhtJzqvE9u5Oh3TwvQxnxsI8RmKFNeSlNZOxOKCLhx.", Role.USER));
        var member2 = memberRepository.save(new Member("dam@email.com", "$2a$10$ISTjRWOs8SFhtJzqvE9u5Oh3TwvQxnxsI8RmKFNeSlNZOxOKCLhx.", Role.USER));

        var product1 = productRepository.save(new Product("eggs", 3990, "https://cdn.pixabay.com/photo/2018/07/23/18/43/hens-egg-3557544_1280.jpg"));
        var product2 = productRepository.save(new Product("hamburger", 5000, "https://cdn.pixabay.com/photo/2018/07/23/18/43/hens-egg-3557544_1280.jpg"));

        var wish1 = wishRepository.save(new Wish(member1, product1, 1));
        var wish2 = wishRepository.save(new Wish(member1, product2, 1));

        var wishes = wishRepository.findAllByMemberId(member1.getId());
        assertThat(wishes.size()).isEqualTo(2); // wish가 2개

        // wish1
        assertThat(wishes.get(0).getId()).isEqualTo(wish1.getId());   // wish id
        assertThat(wishes.get(0).getMember()).isSameAs(member1);      // wish member
        assertThat(wishes.get(0).getProduct()).isSameAs(product1);    // product
        assertThat(wishes.get(0).getQuantity()).isEqualTo(1); // quantity

        // wish2
        assertThat(wishes.get(1).getId()).isEqualTo(wish2.getId());   // wish id
        assertThat(wishes.get(1).getMember()).isSameAs(member1);      // wish member
        assertThat(wishes.get(1).getProduct()).isSameAs(product2);    // product
        assertThat(wishes.get(1).getQuantity()).isEqualTo(1); // quantity

        // wish가 없는 member
        var emptyWishes = wishRepository.findAllByMemberId(member2.getId());
        assertThat(emptyWishes).isEqualTo(List.of());
    }

    @Test
    void deleteByIdAndMemberId() {
        var member1 = memberRepository.save(new Member("sam@email.com", "$2a$10$ISTjRWOs8SFhtJzqvE9u5Oh3TwvQxnxsI8RmKFNeSlNZOxOKCLhx.", Role.USER));
        var member2 = memberRepository.save(new Member("dam@email.com", "$2a$10$ISTjRWOs8SFhtJzqvE9u5Oh3TwvQxnxsI8RmKFNeSlNZOxOKCLhx.", Role.USER));

        var product1 = productRepository.save(new Product("eggs", 3990, "https://cdn.pixabay.com/photo/2018/07/23/18/43/hens-egg-3557544_1280.jpg"));
        var product2 = productRepository.save(new Product("hamburger", 5000, "https://cdn.pixabay.com/photo/2018/07/23/18/43/hens-egg-3557544_1280.jpg"));

        var wish1 = wishRepository.save(new Wish(member1, product1, 1));
        var wish2 = wishRepository.save(new Wish(member1, product2, 1));

        // 상품 삭제 전
        var wishesBeforeDelete = wishRepository.findAllByMemberId(member1.getId());
        assertThat(wishesBeforeDelete.size()).isEqualTo(2);
        
        // 상품 삭제
        wishRepository.deleteByIdAndMemberId(wish1.getId(), member1.getId());
        
        // 상품 삭제 후
        var wishesAfterDelete = wishRepository.findAllByMemberId(member1.getId());
        assertThat(wishesAfterDelete.size()).isEqualTo(1);
        
        // 삭제한 위시 리스트 상품 검색 - null 반환
        var wish = wishRepository.findById(wish1.getId());
        assertThat(wish).isEmpty();
    }
}
