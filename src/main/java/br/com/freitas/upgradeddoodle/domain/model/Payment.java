package br.com.freitas.upgradeddoodle.domain.model;

import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentMethod;
import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentStatus;
import br.com.freitas.upgradeddoodle.presentation.exceptions.BusinessException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "tb_payments")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private Instant paidAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    private String transactionId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @OneToOne(mappedBy = "payment")
    private Order order;

    public static Payment create(Order order, PaymentMethod method) {
        return Payment.builder()
                .order(order)
                .amount(order.getTotal())
                .status(PaymentStatus.PENDING)
                .method(method)
                .build();
    }

    public void authorize(String transactionId) {
        if (this.status != PaymentStatus.PENDING) {
            throw new BusinessException("Only pending payments can be authorized.");
        }

        this.status = PaymentStatus.AUTHORIZED;
        this.transactionId = transactionId;
    }

    public void capture(Instant capturedAt) {
        if (this.status != PaymentStatus.AUTHORIZED) {
            throw new BusinessException("Only authorized payments can be captured.");
        }

        this.status = PaymentStatus.CAPTURED;
        this.paidAt = capturedAt;
    }

    public void cancel() {
        if (this.status == PaymentStatus.CAPTURED || this.status == PaymentStatus.REFUNDED) {
            throw new BusinessException("Captured or refunded payments cannot be cancelled.");
        }

        this.status = PaymentStatus.CANCELLED;
    }

    public void refund() {
        if (this.status != PaymentStatus.CAPTURED) {
            throw new BusinessException("Only captured payments can be refunded.");
        }

        this.status = PaymentStatus.REFUNDED;
    }

    public void complete(String transactionId, Instant moment) {
        this.status = PaymentStatus.CAPTURED;
        this.paidAt = moment;
        this.transactionId = transactionId;
    }
}
