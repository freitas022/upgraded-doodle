# Upgraded Doodle

Marketplace backend com fluxo completo de pedidos e pagamentos (cartão de crédito e Pix), construído com Java 21 e Spring Boot 4.

## Tecnologias

- **Java 21**
- **Spring Boot 4**
- **PostgreSQL 15**
- **Hibernate / Spring Data JPA**
- **Bean Validation**
- **Spring Mail**
- **Lombok**
- **Gradle**

## Arquitetura

O projeto segue uma arquitetura em camadas inspirada em Domain-Driven Design:

```
src/
└── main/
    └── java/br/com/freitas/upgradeddoodle/
        ├── domain/
        │   ├── model/          # Entidades e enums de domínio
        │   └── service/        # Regras de negócio
        ├── infrastructure/
        │   └── repository/     # Repositórios Spring Data JPA
        └── presentation/
            ├── controller/     # Endpoints REST
            ├── dto/            # Records de entrada e saída
            └── exceptions/     # Tratamento global de erros
```

## Funcionalidades

- Cadastro e listagem de produtos
- Criação e gerenciamento de pedidos
- Fluxo de pagamento com cartão de crédito (autorização → captura) e Pix
- Cancelamento e reembolso de pagamentos
- Envio de e-mail transacional

## Como rodar

### Pré-requisitos

- Java 21
- Docker e Docker Compose

### 1. Suba o banco de dados

```bash
docker-compose up -d
```

O PostgreSQL ficará disponível em `localhost:5432` com o banco `upgraded-doodle-app`.

| Variável         | Valor            |
|------------------|------------------|
| `POSTGRES_DB`    | upgraded-doodle-app |
| `POSTGRES_USER`  | postgres         |
| `POSTGRES_PASSWORD` | password      |

### 2. Rode a aplicação

```bash
./gradlew bootRun
```

A API ficará disponível em `http://localhost:8080`.

## Fluxo de Pagamento

```
PENDING → AUTHORIZED → CAPTURED → REFUNDED
                  ↘
               CANCELLED
```

| Método     | Transição                          |
|------------|------------------------------------|
| `authorize` | `PENDING → AUTHORIZED`            |
| `capture`   | `AUTHORIZED → CAPTURED`           |
| `complete`  | `PENDING → CAPTURED` (Pix)        |
| `cancel`    | `PENDING / AUTHORIZED → CANCELLED`|
| `refund`    | `CAPTURED → REFUNDED`             |

## Convenções de código

- **SOLID** aplicado em todas as camadas
- **Records** para DTOs de entrada e saída
- **Constructor Injection** — field injection não utilizado
- Lógica de negócio exclusivamente em Services
- Controllers responsáveis apenas pelo mapeamento HTTP
