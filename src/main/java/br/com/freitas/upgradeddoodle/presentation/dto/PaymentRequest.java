package br.com.freitas.upgradeddoodle.presentation.dto;

import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentMethod;

import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.CardData;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.PixData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(

        @NotNull
        PaymentMethod method,

        @Valid
        CardData card,

        @Valid
        PixData pix
) {

    @JsonIgnore
    @AssertTrue(message = "Invalid payment payload for selected method")
    public boolean isValidByMethod() {
        if (method == null) {
            return true;
        }

        return switch (method) {
            case CREDIT_CARD -> card != null && pix == null;
            case PIX -> pix != null && card == null;
        };
    }
}
