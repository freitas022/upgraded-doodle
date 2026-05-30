package br.com.freitas.upgradeddoodle.domain.service;

import br.com.freitas.upgradeddoodle.domain.event.CustomerRegisteredEvent;
import br.com.freitas.upgradeddoodle.domain.model.Customer;
import br.com.freitas.upgradeddoodle.domain.model.enums.AccountStatus;
import br.com.freitas.upgradeddoodle.domain.repository.CustomerRepository;
import br.com.freitas.upgradeddoodle.presentation.dto.CustomerRequest;
import br.com.freitas.upgradeddoodle.presentation.exceptions.BusinessException;
import br.com.freitas.upgradeddoodle.presentation.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void register(CustomerRequest request) {
        var customer = request.toEntity();
        customer.register();
        customer.getAccount().generateActivationToken();
        customerRepository.save(customer);
        eventPublisher.publishEvent(new CustomerRegisteredEvent(customer));
    }

    @Transactional
    public void activateAccount(String token) {
        Customer customer = customerRepository.findByAccountVerificationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token de ativação inválido ou inexistente."));

        customer.getAccount().activate(token);

        customerRepository.save(customer);
    }
}
