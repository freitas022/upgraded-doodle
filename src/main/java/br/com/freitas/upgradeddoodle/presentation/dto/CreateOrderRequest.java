package br.com.freitas.upgradeddoodle.presentation.dto;

import java.util.List;

public record CreateOrderRequest(

        Long customerId,
        List<CreateOrderItemRequest> items
) {
}
