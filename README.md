# Mini Wallet Transaction System

A RESTful wallet management application built with Java and Spring Boot. The application allows users to create wallets, deposit money, transfer funds between wallets, and view transaction history.

## Features

- Create and retrieve wallets
- Deposit money into a wallet
- Transfer funds between wallets
- View wallet transaction history
- Maintain transaction records for deposits and transfers
- Use `BigDecimal` for precise monetary calculations
- Manage database operations using Spring Data JPA and Hibernate
- Use transaction management to maintain consistency during transfers
- Integration testing with JUnit 5 and H2

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot
- **Database:** H2
- **ORM:** Hibernate / Spring Data JPA
- **Build Tool:** Maven
- **API Documentation:** SpringDoc / Swagger UI
- **Testing:** JUnit 5, Spring Boot Test

## Prerequisites

- Java Development Kit (JDK) 21
- Maven, or the Maven Wrapper included in the project

Verify Java installation:

```bash
java -version
```

## Getting Started

### 1. Clone the repository

```bash
git clone YOUR_GITHUB_REPOSITORY_URL
cd mini-wallet
```

Replace `YOUR_GITHUB_REPOSITORY_URL` with your actual GitHub repository URL.

### 2. Run the application

Using the Maven Wrapper on Linux/macOS:

```bash
./mvnw spring-boot:run
```

On Windows:

```bat
mvnw.cmd spring-boot:run
```

Alternatively, if Maven is installed:

```bash
mvn spring-boot:run
```

The application starts at:

`http://localhost:8080`

## Database Configuration

The application uses a file-based H2 database for local development.

```properties
spring.datasource.url=jdbc:h2:file:./data/walletdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

server.port=8080
```

### H2 Database Console

Open:

`http://localhost:8080/h2-console`

Connection settings:

| Property | Value |
|---|---|
| JDBC URL | `jdbc:h2:file:./data/walletdb` |
| Username | `sa` |
| Password | Leave blank |

The integration test uses a separate in-memory H2 database to isolate test data from the persistent development database.

## REST API Endpoints

Base URL: `http://localhost:8080`

| HTTP Method | Endpoint | Description |
|---|---|---|
| POST | `/api/wallets` | Create a wallet |
| GET | `/api/wallets/{walletId}` | Retrieve wallet details |
| POST | `/api/wallets/{walletId}/deposit` | Deposit money |
| POST | `/api/wallets/transfer` | Transfer money between wallets |
| GET | `/api/wallets/{walletId}/transactions` | Retrieve transaction history |

### Example: Create a Wallet

**Request**

```http
POST /api/wallets
Content-Type: application/json
```

```json
{
  "name": "Rahul",
  "email": "rahul@example.com",
  "initialBalance": 1000.00
}
```

### Example: Deposit Money

**Request**

```http
POST /api/wallets/1/deposit
Content-Type: application/json
```

```json
{
  "amount": 500.00
}
```

### Example: Transfer Money

**Request**

```http
POST /api/wallets/transfer
Content-Type: application/json
```

```json
{
  "fromWalletId": 1,
  "toWalletId": 2,
  "amount": 300.00
}
```

### Example: Retrieve Transaction History

```http
GET /api/wallets/1/transactions
```

The history endpoint is intended to return transactions associated with the requested wallet, with the newest transactions first.

> **Note:** The endpoint paths and JSON examples follow the project's assignment contract. Confirm that they match the actual controller mappings and DTO fields before publishing.

## Running Tests

Run the complete test suite:

```bash
./mvnw test
```

Run the wallet transfer integration test specifically:

```bash
./mvnw -Dtest=WalletTransferIntegrationTest test
```

If Maven is installed but the Maven Wrapper is not available:

```bash
mvn test
```

The transfer integration test verifies balance updates and preservation of the combined wallet balance. The insufficient-balance test verifies that a rejected transfer leaves both wallet balances unchanged.

## Transfer Consistency

For a transfer of `300.00` from a wallet with `1000.00` to a wallet with `500.00`:

| Wallet | Before | After |
|---|---:|---:|
| Sender | 1000.00 | 700.00 |
| Receiver | 500.00 | 800.00 |
| **Total** | **1500.00** | **1500.00** |

The transfer should update both wallets and record the corresponding transaction history consistently. If processing fails, the operation should roll back rather than leave a partially completed transfer.

## API Documentation

If SpringDoc is configured, start the application and visit:

- **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI specification:** `http://localhost:8080/v3/api-docs`

Swagger UI allows you to explore and test the available endpoints.

## Project Structure

```text
mini-wallet/
├── src/
│   ├── main/
│   │   ├── java/com/example/mini_wallet/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/example/mini_wallet/
│           └── WalletTransferIntegrationTest.java
├── data/
├── pom.xml
└── README.md
```

Folder names may vary slightly depending on the actual project structure.

## Future Improvements

- Add authentication and authorization
- Add pagination for transaction history
- Add idempotency protection for transfer requests
- Add more integration tests for rollback and concurrent transfers
- Use a production-grade database for deployment

## License

This project was developed for learning and assessment purposes.
