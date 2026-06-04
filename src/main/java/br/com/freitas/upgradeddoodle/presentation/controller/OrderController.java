package br.com.freitas.upgradeddoodle.presentation.controller;

import br.com.freitas.upgradeddoodle.domain.service.OrderService;
import br.com.freitas.upgradeddoodle.presentation.dto.CreateOrderRequest;
import br.com.freitas.upgradeddoodle.presentation.dto.OrderCreatedResponse;
import br.com.freitas.upgradeddoodle.presentation.dto.OrderDetailResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderCreatedResponse> create(@RequestBody @Valid CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> findById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(orderService.findOrderDetailsById(id));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<OrderDetailResponse> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.confirm(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderDetailResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancel(id));
    }
}
