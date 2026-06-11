package br.com.freitas.upgradeddoodle.domain.service;

import br.com.freitas.upgradeddoodle.domain.event.PaymentAuthorizationFailedEvent;
import br.com.freitas.upgradeddoodle.domain.event.PaymentCaptureFailedEvent;
import br.com.freitas.upgradeddoodle.domain.event.PaymentCapturedEvent;
import br.com.freitas.upgradeddoodle.domain.model.Order;
import br.com.freitas.upgradeddoodle.domain.model.Payment;
import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentMethod;
import br.com.freitas.upgradeddoodle.domain.model.enums.PaymentStatus;
import br.com.freitas.upgradeddoodle.domain.repository.PaymentRepository;
import br.com.freitas.upgradeddoodle.infrastructure.payment.adapter.PaymentProcessor;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.PaymentResponse;
import br.com.freitas.upgradeddoodle.presentation.dto.PaymentRequest;
import br.com.freitas.upgradeddoodle.presentation.exceptions.BusinessException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final Map<PaymentMethod, PaymentProcessor> processors;
    private final ApplicationEventPublisher eventPublisher;

    public PaymentService(
            PaymentRepository paymentRepository,
            List<PaymentProcessor> processors,
            ApplicationEventPublisher eventPublisher
    ) {
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
        this.processors = processors.stream()
                .collect(Collectors.toMap(
                        PaymentProcessor::supports,
                        Function.identity(),
                        (a, b) -> {
                            throw new BusinessException(
                                    "Duplicate PaymentProcessor for method " + a.supports()
                            );
                        }
                ));
    }

    @Transactional
    public PaymentResponse processPaymentForOrder(@NotNull Order order, @NotNull PaymentRequest request) {
        log.info("Processing payment for order {}", order.getId());

        if (!order.isConfirmed()) {
            throw new BusinessException("Order must be confirmed before processing payment.");
        }

        var payment = Payment.create(order, request.method());
        order.assignPayment(payment);
        paymentRepository.save(payment);

        var processor = resolveProcessor(request.method());

        try {
            processor.process(order, payment, request);
        } catch (BusinessException ex) {
            handleFailure(payment, order.getId(), ex);
            throw ex;
        }

        paymentRepository.save(payment);

        if (payment.getStatus().equals(PaymentStatus.CAPTURED)) {
            order.markAsPaid();
            eventPublisher.publishEvent(new PaymentCapturedEvent(
                    payment.getId(),
                    order.getId()
            ));
        }

        return PaymentResponse.from(
                payment,
                order,
                request.method()
        );
    }

    private PaymentProcessor resolveProcessor(PaymentMethod method) {
        return Optional.ofNullable(processors.get(method))
                .orElseThrow(() -> new BusinessException("Unsupported payment method: " + method));
    }

    private void handleFailure(Payment payment, Long orderId, BusinessException ex) {
        payment.cancel();
        paymentRepository.save(payment);

        if (payment.getTransactionId() == null) {
            eventPublisher.publishEvent(new PaymentAuthorizationFailedEvent(
                    payment.getId(),
                    orderId,
                    ex.getMessage()
            ));
            return;
        }

        eventPublisher.publishEvent(new PaymentCaptureFailedEvent(
                payment.getId(),
                orderId,
                ex.getMessage()
        ));
    }
}
