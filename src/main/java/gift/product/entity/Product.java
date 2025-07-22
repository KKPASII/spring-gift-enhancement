package gift.product.entity;

import gift.product.dto.ProductRequestDto;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 15)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "image_url", nullable = false, length = 1024)
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Option> options = new ArrayList<>();

    protected Product() {}

    public Product(String name, int price, String imageUrl) {
        this(null, name, price, imageUrl);
    }

    public Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public List<Option> getOptions() {
        return this.options;
    }

    public void update(ProductRequestDto productRequestDto) {
        this.name = productRequestDto.getName();
        this.price = productRequestDto.getPrice();
        this.imageUrl = productRequestDto.getImageUrl();
    }

    public void addOption(Option option) {
        if (!this.options.contains(option)) {
            this.options.add(option);
        }
        option.setProduct(this);
    }

    public void checkDuplicatedName(String name) {
        boolean isDuplicated = this.options.stream()
            .anyMatch(option -> option.getName().equals(name));
        if (isDuplicated) {
            throw new IllegalArgumentException("옵션 이름이 이미 존재합니다.");
        }
    }
}
