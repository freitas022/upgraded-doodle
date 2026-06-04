package br.com.freitas.upgradeddoodle.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "tb_order_items")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public static OrderItem create(Product product, Integer quantity, BigDecimal price) {
        return OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .price(price.setScale(2, RoundingMode.HALF_EVEN))
                .discount(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN))
                .build();
    }

    public BigDecimal getSubTotal() {
        BigDecimal gross = price.multiply(BigDecimal.valueOf(quantity));
        return gross.subtract(discount)
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem other = (OrderItem) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
