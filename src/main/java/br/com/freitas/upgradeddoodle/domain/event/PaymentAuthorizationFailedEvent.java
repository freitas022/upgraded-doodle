package br.com.freitas.upgradeddoodle.domain.event;

import br.com.freitas.upgradeddoodle.domain.model.Payment;

public record PaymentAuthorizationFailedEvent(

        Payment payment,
        Exception authException
) {
}
