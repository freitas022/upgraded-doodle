package br.com.freitas.upgradeddoodle.infrastructure.payment.dto;

import jakarta.validation.constraints.NotBlank;

public record PixData(

        @NotBlank
        String payerName,

        @NotBlank
        String payerDocument,

        @NotBlank
        String pixKey
) {}
