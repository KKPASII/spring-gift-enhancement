package gift.product.entity;

import jakarta.persistence.*;

@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Option() {}

    public Option(String name, int quantity) {
        validate(name, quantity);
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return this.id;
    }

    public Product getProduct() {
        return this.product;
    }

    public String getName() {
        return this.name;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void subtractQuantity(int amount) {
        int quantity = this.quantity - amount;
        if (quantity < 1) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.quantity = quantity;
    }

    private void validate(String name, int quantity) {
        if (name == null || name.trim().isEmpty() || name.length() > 50) {
            throw new IllegalArgumentException("옵션 이름은 1자 이상 50자 이하로 입력해주세요.");
        }
        if (!name.matches("^[a-zA-Z0-9가-힣\\s()\\[\\]+\\-&/_ ]*$")) {
            throw new IllegalArgumentException("옵션 이름은 공백과 특수문자((), [], +, -, &, /, _)를 포함하여 작성할 수 있습니다.");
        }
        if (quantity < 1 || quantity >= 100_000_000) {
            throw new IllegalArgumentException("옵션 수량은 1개 이상 1억 개 미만이어야 합니다.");
        }
    }
}
