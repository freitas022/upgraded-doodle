package br.com.freitas.upgradeddoodle.infrastructure.payment.dto;

import java.math.BigDecimal;

public record PaymentCommand(

        String cardToken,
        BigDecimal amount,
        Long orderId,
        String customerEmail
) {
}
