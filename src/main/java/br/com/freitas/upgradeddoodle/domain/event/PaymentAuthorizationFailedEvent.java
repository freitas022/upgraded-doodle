package br.com.freitas.upgradeddoodle.domain.event;

public record PaymentAuthorizationFailedEvent(

        Long paymentId,
        Long orderId,
        String reason
) {
}
