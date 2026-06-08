package br.com.freitas.upgradeddoodle.domain.event;

import br.com.freitas.upgradeddoodle.domain.model.Payment;

public record PaymentCaptureFailedEvent(

        Payment payment,
        Exception captureException
) {
}
