package br.com.freitas.upgradeddoodle.domain.model;

import br.com.freitas.upgradeddoodle.domain.model.enums.OrderStatus;
import br.com.freitas.upgradeddoodle.presentation.exceptions.BusinessException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public void addItem(OrderItem item) {
        this.items.add(item);
        item.setOrder(this);
    }

    public static Order create(Customer customer) {
        return Order.builder()
                .customer(customer)
                .status(OrderStatus.PENDING)
                .build();
    }

    public boolean isPending() {
        return this.status == OrderStatus.PENDING;
    }

    public boolean isConfirmed() {
        return this.status == OrderStatus.CONFIRMED;
    }

    public boolean isCancelled() {
        return this.status == OrderStatus.CANCELLED;
    }

    public void markAsPaid() {
        if (!isConfirmed()) {
            throw new BusinessException("Only confirmed orders can be marked as paid.");
        }

        this.status = OrderStatus.PAID;
    }

    public void confirm() {
        if (this.items.isEmpty()) {
            throw new BusinessException("Order must have at least one item.");
        }

        if (!isPending()) {
            throw new BusinessException("Only pending orders can be confirmed.");
        }

        this.status = OrderStatus.CONFIRMED;
    }

    public void cancel() {
        if (isCancelled()) {
            throw new BusinessException("Order is already cancelled.");
        }

        if (this.status == OrderStatus.SHIPPED || this.status == OrderStatus.DELIVERED) {
            throw new BusinessException("Order cannot be cancelled after shipping.");
        }

        this.status = OrderStatus.CANCELLED;
    }

    public void assignPayment(Payment payment) {
        this.payment = payment;
    }

    public BigDecimal getTotal() {
        return items.stream()
                .map(OrderItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_EVEN);
    }
}
