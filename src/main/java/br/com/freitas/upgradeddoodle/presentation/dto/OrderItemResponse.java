package br.com.freitas.upgradeddoodle.presentation.dto;

import br.com.freitas.upgradeddoodle.domain.model.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal price,
        BigDecimal discount,
        BigDecimal subTotal
) {
    public static OrderItemResponse fromEntity(OrderItem item) {
        return new OrderItemResponse(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPrice(),
                item.getDiscount(),
                item.getSubTotal()
        );
    }
}
