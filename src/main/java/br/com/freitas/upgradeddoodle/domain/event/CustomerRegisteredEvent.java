package br.com.freitas.upgradeddoodle.domain.event;

import br.com.freitas.upgradeddoodle.domain.model.Customer;

public record CustomerRegisteredEvent(Customer customer) {
}
