package br.com.freitas.upgradeddoodle.domain.service;

import br.com.freitas.upgradeddoodle.domain.model.Payment;
import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentStatus;
import br.com.freitas.upgradeddoodle.domain.repository.PaymentRepository;
import br.com.freitas.upgradeddoodle.infrastructure.payment.adapter.PaymentGateway;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.AuthorizationCommand;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.AuthorizationResult;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.CardData;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.CurrencyCode;
import br.com.freitas.upgradeddoodle.presentation.exceptions.BusinessException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class PaymentAuthorizationService {

    private final PaymentGateway paymentGateway;
    private final PaymentRepository paymentRepository;

    public void authorize(@NotNull Payment payment, @NotNull @Valid CardData card) {
        log.info("Authorizing payment {}", payment.getId());

        var command = new AuthorizationCommand(
                payment.getId(),
                payment.getAmount(),
                CurrencyCode.BRL,
                card
        );

        var result = paymentGateway.authorize(command);

        ensureAuthorized(result);

        payment.authorize(result.transactionId());
        paymentRepository.save(payment);
    }

    private void ensureAuthorized(AuthorizationResult result) {
        if (!PaymentStatus.AUTHORIZED.equals(result.status())) {
            throw new BusinessException("Payment authorization failed");
        }
    }
}
