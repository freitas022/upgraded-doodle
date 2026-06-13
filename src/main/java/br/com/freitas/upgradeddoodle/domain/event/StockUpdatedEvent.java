package br.com.freitas.upgradeddoodle.domain.event;

public record StockUpdatedEvent(
        Long productId,
        Integer quantityAvailable
) {}
