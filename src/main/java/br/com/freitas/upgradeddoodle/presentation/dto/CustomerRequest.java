package br.com.freitas.upgradeddoodle.presentation.dto;

import br.com.freitas.upgradeddoodle.domain.model.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

public record CustomerRequest(

        @NotBlank(message = "O nome é obrigatório.")
        String name,

        @NotBlank(message = "O telefone é obrigatório.")
        @Pattern(regexp = "^[1-9]{2}9[2-9]\\d{7}$",  message = "O número deve ser válido e conter exatamente 11 dígitos com DDD (ex: 11999998888).")
        String phone,

        @NotBlank(message = "O documento é obrigatório.")
        @CPF(message = "O CPF é inválido.")
        String document,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "O email é inválido.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        String password
) {

    public Customer toEntity() {
        var customer = new Customer();
        customer.setName(this.name());
        customer.setPhone(this.phone());
        customer.setEmail(this.email());
        customer.setDocument(this.document());
        customer.setPassword(this.password());

        return customer;
    }
}
