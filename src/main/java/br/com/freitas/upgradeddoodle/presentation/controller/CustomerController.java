package br.com.freitas.upgradeddoodle.presentation.controller;

import br.com.freitas.upgradeddoodle.domain.service.CustomerService;
import br.com.freitas.upgradeddoodle.presentation.dto.CustomerRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody CustomerRequest request) {
        customerService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/activate")
    public ResponseEntity<?> activateAccount(@RequestParam String token) {
        customerService.activateAccount(token);
        return ResponseEntity.noContent().build();
    }
}
