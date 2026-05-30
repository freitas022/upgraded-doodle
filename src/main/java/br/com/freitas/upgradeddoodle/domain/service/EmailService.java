package br.com.freitas.upgradeddoodle.domain.service;

import br.com.freitas.upgradeddoodle.domain.model.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Async
    public void sendActivationEmail(Customer customer) {
        /*
        String activationUrl = "http://localhost:8080/api/v1/customers/activate?token="
                + customer.getAccount().getVerificationToken();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(customer.getEmail());
        message.setSubject("Ativação de Conta - Upgraded Doodle");
        message.setText("Olá " + customer.getName() + ",\n\n" +
                "Obrigado por se cadastrar! Clique no link abaixo para ativar sua conta:\n" +
                activationUrl + "\n\n" +
                "O link é válido por 24 horas.");

        mailSender.send(message);*/
        log.info("Enviando e-mail de ativação para o cliente {}", customer.getEmail());
    }

    public void sendPasswordRecoveryEmail(Customer customer) {
        /*
        String recoveryUrl = "http://localhost:8080/api/v1/customers/recover?token="
                + customer.getAccount().getVerificationToken();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(customer.getEmail());
        message.setSubject("Recuperação de Senha - Upgraded Doodle");
        message.setText("Olá " + customer.getName() + ",\n\n" +
                "Recebemos uma solicitação para recuperar sua senha. Clique no link abaixo para criar uma nova senha:\n" +
                recoveryUrl + "\n\n" +
                "O link é válido por 24 horas.");

        mailSender.send(message);*/
        log.info("Enviando e-mail de redefinição de senha para o cliente {}", customer.getEmail());
    }
}
