# Park & Charge – Micro‑services Platform

> **Team Receding Pipeline**
> Yanal Al Halabi • Konstantin Smirnov • Anna Bezuhla • Sarvar Abdurakhimov • Averest Osman

A scalable electric‑vehicle charging‑station booking system built with Spring Boot micro‑services, React on the front‑end, and model‑driven design via **LEMMA**.

---

## Architecture

### Core Micro‑services

| Service               | Port     | Purpose                                            |
| --------------------- | -------- | -------------------------------------------------- |
| **GatewayService**    | **8080** | Single external entry point (Spring Cloud Gateway) |
| **UserService**       | **8081** | User accounts, authentication, balance management  |
| **BookingService**    | **8082** | Create / manage bookings for charging slots        |
| **PaymentService**    | **8083** | Handle balance top‑ups & payment capture           |
| **ChargingService**   | **8084** | CRUD for charging‑station listings                 |
| **StatisticsService** | **8085** | Aggregate usage & revenue metrics                  |

### Shared Infrastructure

| Component        | Port     | Role                                                  |
| ---------------- | -------- | ----------------------------------------------------- |
| **EurekaServer** | **8761** | Service discovery registry                            |
| **ConfigServer** | **8888** | Centralised Spring Cloud Config repository            |
| **H2 Databases** | —        | One embedded H2 instance per micro‑service (dev mode) |

Front‑end: **React SPA** on port **3000** (proxying via Gateway in production).

---

## LEMMA Models

All architecture models live under **`lemma/`**

```
lemma/
 ├─ domain/        # Task A1 – DDD domain models
 ├─ microservices/ # Task A2 – service/API models
 ├─ operation/     # Task A3 – containers & infra topology
 └─ technology/    # Docker, JavaSpring, Protocol definitions
```

Use the LEMMA Eclipse plug‑in or CLI to validate and generate code/compose files:

```bash
# Validate models
java -jar lemma-cli.jar validate lemma/

# Generate Spring code from Domain+Service models
java -jar lemma-cli.jar generate \
  --viewpoints domain,service --technology JavaSpring \
  --models lemma/ --output generated/

# Generate docker‑compose from Operation model
java -jar lemma-cli.jar generate \
  --viewpoints operation --technology Docker \
  --models lemma/ --output deploy/
```

---

## Quick Start

### Prerequisites

* **Java 21**
* **Gradle** (wrapper included)
* **Node 18+**

### 1 — Backend services

Run Eureka, Config Server & Gateway first, then the five core services – each in its own terminal:

```bash
# 1. Service Discovery
cd EurekaServer      && ./gradlew bootRun
# 2. Configuration Server
cd ConfigServer      && ./gradlew bootRun
# 3. API Gateway
cd GatewayService    && ./gradlew bootRun

# 4 ‑ 8. Domain micro‑services
cd UserService       && ./gradlew bootRun        # 8081
cd BookingService    && ./gradlew bootRun        # 8082
cd PaymentService    && ./gradlew bootRun        # 8083
cd ChargingService   && ./gradlew bootRun        # 8084
cd StatisticsService && ./gradlew bootRun        # 8085
```

### 2 — Frontend

```bash
cd frontend
npm install
npm start   # http://localhost:3000 (proxied via Gateway)
```

---

## Current Features

* User registration, login & JWT auth
* Balance top‑up / deduction with payment ledger
* Charging‑station catalogue & owner management
* Real‑time slot availability & booking workflow
* Service discovery, central config & edge routing via Spring Cloud
* Responsive React dashboard
* **Full architecture captured in LEMMA models**

---

## Development Notes

* **Eureka UI:** `http://localhost:8761`
* **H2 console (UserService):** `http://localhost:8081/h2-console`
  *JDBC URL `jdbc:h2:mem:userdb`, user `sa`, no pwd*
* Each micro‑service keeps its own embedded H2 DB; swap to Postgres/MySQL in `application.yml`.

### Project Tree (top level)

```
├── UserService/          # User management micro‑service
├── BookingService/       # Booking management micro‑service
├── ChargingService/      # Charging‑station micro‑service
├── PaymentService/       # Payment processing micro‑service
├── StatisticsService/    # Usage metrics micro‑service
├── EurekaServer/         # Service registry
├── ConfigServer/         # Spring Cloud Config server
├── GatewayService/       # API Gateway
├── frontend/             # React SPA
├── config-repo/          # Externalised *.yml for ConfigServer
└── lemma/                # LEMMA models (Domain/Service/Operation/Technology)
```

---
