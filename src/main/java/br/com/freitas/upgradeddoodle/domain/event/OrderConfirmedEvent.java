package br.com.freitas.upgradeddoodle.domain.event;

public record OrderConfirmedEvent(
        Long orderId,
        String customerEmail
) {
}
