package br.com.freitas.upgradeddoodle.domain.event;

public record OrderCancelledEvent(
        Long orderId,
        String customerEmail
) {
}
