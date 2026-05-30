package br.com.freitas.upgradeddoodle.domain.model;

import br.com.freitas.upgradeddoodle.domain.model.enums.AccountStatus;
import br.com.freitas.upgradeddoodle.presentation.exceptions.BusinessException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "token_expires_at")
    private Instant tokenExpiresAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    public void activate(String token) {
        if (this.tokenExpiresAt.isBefore(Instant.now())) {
            throw new BusinessException("Este link de ativação expirou. Solicite um novo envio.");
        }

        this.status = AccountStatus.ACTIVE;
        this.verificationToken = null;
        this.tokenExpiresAt = null;
    }

    public void suspend() {
        this.status = AccountStatus.SUSPENDED;
    }

    public void generateActivationToken() {
        this.status = AccountStatus.INACTIVE;
        this.verificationToken = UUID.randomUUID().toString();
        this.tokenExpiresAt = Instant.now().plus(24, ChronoUnit.HOURS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
