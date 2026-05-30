package br.com.freitas.upgradeddoodle.domain.service;

import br.com.freitas.upgradeddoodle.domain.model.Address;
import br.com.freitas.upgradeddoodle.domain.model.Customer;
import br.com.freitas.upgradeddoodle.domain.repository.CustomerRepository;
import br.com.freitas.upgradeddoodle.presentation.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final CustomerRepository customerRepository;

    @Transactional
    public void addAddressToCustomer(Long customerId, Address newAddress) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID informado."));

        customer.addAddress(newAddress);
        customerRepository.save(customer);
    }
}
