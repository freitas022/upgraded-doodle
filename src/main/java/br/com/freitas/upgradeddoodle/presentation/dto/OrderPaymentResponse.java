package br.com.freitas.upgradeddoodle.presentation.dto;

import br.com.freitas.upgradeddoodle.domain.model.Payment;

import java.time.Instant;

public record OrderPaymentResponse(
        Long id,
        String method,
        Instant paidAt
) {
    public static OrderPaymentResponse fromEntity(Payment payment) {
        if (payment == null) {
            return null;
        }

        return new OrderPaymentResponse(
                payment.getId(),
                payment.getMethod().name(),
                payment.getPaidAt()
        );
    }
}
