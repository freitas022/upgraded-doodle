package br.com.freitas.upgradeddoodle.infrastructure.payment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CardData(

        @NotBlank
        String holderName,

        @NotBlank
        String number,

        @NotNull
        @Min(1)
        @Max(12)
        Integer expirationMonth,

        @NotNull
        Integer expirationYear,

        @NotBlank
        String cvv,

        @NotNull
        @Min(1)
        Integer installments
) {}
