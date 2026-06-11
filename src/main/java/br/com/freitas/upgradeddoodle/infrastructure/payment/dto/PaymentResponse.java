package br.com.freitas.upgradeddoodle.infrastructure.payment.dto;

import br.com.freitas.upgradeddoodle.domain.model.Order;
import br.com.freitas.upgradeddoodle.domain.model.Payment;
import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentMethod;
import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentStatus;

public record PaymentResponse(
        Long paymentId,
        Long orderId,
        PaymentMethod method,
        PaymentStatus status,
        String transactionId,
        String message
) {
    public static PaymentResponse from(Payment payment, Order order, PaymentMethod method) {
        return new PaymentResponse(
                payment.getId(),
                order.getId(),
                method,
                payment.getStatus(),
                payment.getTransactionId(),
                buildMessage(payment)
        );
    }

    private static String buildMessage(Payment payment) {
        return switch (payment.getStatus()) {
            case CAPTURED -> "Payment captured successfully";
            case AUTHORIZED -> "Payment authorized successfully";
            case CANCELLED-> "Payment canceled";
            case REFUNDED -> "Payment refunded successfully";
            default -> "Payment processed";
        };
    }
}
