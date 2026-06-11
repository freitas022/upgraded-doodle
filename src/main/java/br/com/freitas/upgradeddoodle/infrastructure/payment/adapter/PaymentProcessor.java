package br.com.freitas.upgradeddoodle.infrastructure.payment.adapter;

import br.com.freitas.upgradeddoodle.domain.model.Order;
import br.com.freitas.upgradeddoodle.domain.model.Payment;
import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentMethod;
import br.com.freitas.upgradeddoodle.presentation.dto.PaymentRequest;

public interface PaymentProcessor {

    PaymentMethod supports();
    void process(Order order, Payment payment, PaymentRequest request);
}
