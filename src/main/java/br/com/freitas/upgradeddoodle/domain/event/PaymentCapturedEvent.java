package br.com.freitas.upgradeddoodle.domain.event;

public record PaymentCapturedEvent(Long paymentId, Long orderId) {
}