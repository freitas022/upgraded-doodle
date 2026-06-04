package br.com.freitas.upgradeddoodle.presentation.dto;

public record CreateOrderItemRequest(

        Long productId,
        Integer quantity
) {
}
