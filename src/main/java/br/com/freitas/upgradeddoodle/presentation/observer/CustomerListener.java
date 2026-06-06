package br.com.freitas.upgradeddoodle.presentation.observer;

import br.com.freitas.upgradeddoodle.domain.event.CustomerRegisteredEvent;
import br.com.freitas.upgradeddoodle.domain.event.ForgotPasswordRequestedEvent;
import br.com.freitas.upgradeddoodle.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CustomerListener {

    private final EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCustomerRegisteredEvent(CustomerRegisteredEvent event) {
        emailService.sendActivationEmail(event.customer());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onForgotPasswordEvent(ForgotPasswordRequestedEvent event) {
        emailService.sendPasswordRecoveryEmail(event.customer());
    }
}
