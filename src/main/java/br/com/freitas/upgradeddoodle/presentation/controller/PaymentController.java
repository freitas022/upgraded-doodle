package br.com.freitas.upgradeddoodle.presentation.controller;

import br.com.freitas.upgradeddoodle.domain.service.OrderService;
import br.com.freitas.upgradeddoodle.domain.service.PaymentService;
import br.com.freitas.upgradeddoodle.infrastructure.payment.dto.PaymentResponse;
import br.com.freitas.upgradeddoodle.presentation.dto.PaymentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    @PostMapping("/orders/{orderId}")
    public ResponseEntity<PaymentResponse> processPayment(
                                                        @PathVariable Long orderId,
                                                        @Valid @RequestBody PaymentRequest request) {
        var order = orderService.findById(orderId);
        return ResponseEntity.ok(paymentService.processPaymentForOrder(order, request));
    }
}
