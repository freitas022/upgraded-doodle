package br.com.freitas.upgradeddoodle.infrastructure.payment.adapter;

import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.*;
import jakarta.validation.Valid;

public interface PaymentGateway {

    @Valid
    AuthorizationResult authorize(AuthorizationCommand command);

    @Valid
    CaptureResult capture(CaptureCommand command);

    @Valid
    RefundResult refund(RefundCommand command);
}
