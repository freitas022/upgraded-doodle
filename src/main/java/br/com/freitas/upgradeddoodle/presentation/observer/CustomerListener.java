package br.com.freitas.upgradeddoodle.presentation.observer;

import br.com.freitas.upgradeddoodle.domain.event.CustomerRegisteredEvent;
import br.com.freitas.upgradeddoodle.domain.event.ForgotPasswordRequestedEvent;
import br.com.freitas.upgradeddoodle.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerListener {

    private final EmailService emailService;

    @Async
    @EventListener
    public void handleCustomerRegisteredEvent(CustomerRegisteredEvent event) {
        emailService.sendActivationEmail(event.customer());
    }

    @Async
    @EventListener
    public void handleForgotPassword(ForgotPasswordRequestedEvent event) {
        emailService.sendPasswordRecoveryEmail(event.customer());
    }
}
