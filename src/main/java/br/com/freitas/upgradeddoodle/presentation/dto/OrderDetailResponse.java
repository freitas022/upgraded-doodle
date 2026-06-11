package br.com.freitas.upgradeddoodle.presentation.dto;

import br.com.freitas.upgradeddoodle.domain.model.Order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderDetailResponse(
        Long id,
        String status,
        BigDecimal total,
        Instant createdAt,
        List<OrderItemResponse> items,
        OrderPaymentResponse payment
) {
    public static OrderDetailResponse fromEntity(Order order) {
        return new OrderDetailResponse(
                order.getId(),
                order.getStatus().name(),
                order.getTotal(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(OrderItemResponse::fromEntity)
                        .toList(),
                OrderPaymentResponse.fromEntity(order.getPayment())
        );
    }
}