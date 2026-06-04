package br.com.freitas.upgradeddoodle.presentation.dto;

import br.com.freitas.upgradeddoodle.domain.model.Order;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderCreatedResponse(
        Long id,
        String status,
        BigDecimal total,
        Instant createdAt
) {
    public static OrderCreatedResponse fromEntity(Order order) {
        return new OrderCreatedResponse(
                order.getId(),
                order.getStatus().name(),
                order.getTotal(),
                order.getCreatedAt()
        );
    }
}
