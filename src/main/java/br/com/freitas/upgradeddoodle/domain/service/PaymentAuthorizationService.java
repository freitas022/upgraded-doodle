package br.com.freitas.upgradeddoodle.domain.service;

import br.com.freitas.upgradeddoodle.domain.model.Payment;
import br.com.freitas.upgradeddoodle.domain.repository.PaymentRepository;
import br.com.freitas.upgradeddoodle.infrastructure.payment.adapter.PaymentGateway;
import br.com.freitas.upgradeddoodle.presentation.dto.PaymentCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentAuthorizationService {

    private final PaymentGateway paymentGateway;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void authorize(Payment payment, String cardToken) {
        var order = payment.getOrder();
        var customer = order.getCustomer();

        var command = new PaymentCommand(
                cardToken,
                payment.getAmount(),
                order.getId(),
                customer.getEmail()
        );

        var result = paymentGateway.authorize(command);

        payment.authorize(result.transactionId());
        paymentRepository.save(payment);
    }
}
