package br.com.freitas.upgradeddoodle.domain.event;

public record PaymentCaptureFailedEvent(

        Long paymentId,
        Long orderId,
        String reason
) {
}
