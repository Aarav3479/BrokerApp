# BrokerApp

## Introduction

**BrokerApp** is an event-driven, microservices-based broker platform that simulates the core workflow of a real-world trading system — from user authentication and order placement to trade execution and portfolio updates. The system is built using **Spring Boot microservices**, **Apache Kafka** for asynchronous communication, **PostgreSQL** with database-per-service isolation, and **JWT-based stateless authentication**, closely mirroring production-grade financial architectures.

The primary goal of this project is to demonstrate **system design thinking**, **event-driven workflows**, and **fault-tolerant microservice communication**, rather than simple CRUD-based APIs. Each service has a clearly defined responsibility, communicates through well-defined domain events, and is independently deployable and scalable.

---

## High-Level Architecture

The platform consists of the following microservices:

- **User Service** – Authentication and user identity management  
- **Order Service** – Order validation and placement  
- **Trade Service** – In-memory order matching and trade execution  
- **Portfolio Service** – Portfolio state management and holdings calculation  
- **Discovery Service (Eureka)** – Service discovery and registration  

Each service:
- Owns its **own database**
- Is **Dockerized**
- Registers with **Eureka**
- Communicates primarily via **Kafka**

---

## End-to-End Code Flow

### 1. User Registration & Authentication

The flow begins in the **User Service**, which acts as the authentication authority of the system. Users register and log in using email and password credentials. Passwords are securely hashed using **BCrypt**, and upon successful login, the service issues a **JWT (HS256, 1-hour expiry)**.

The JWT is required for all protected endpoints across services and is validated using a custom JWT filter integrated into Spring Security. This ensures **stateless authentication** across the entire platform.

On successful registration, the User Service publishes a **`user-created`** event to Kafka. Similarly, when a user is deleted, a **`user-deleted`** event is emitted.

---

### 2. Portfolio Initialization via Events

The **Portfolio Service** consumes `user-created` and `user-deleted` events asynchronously. When a `user-created` event is received, a new portfolio is automatically initialized for the user with zero holdings. When a `user-deleted` event is received, the corresponding portfolio is removed.

This design eliminates synchronous dependencies between services and ensures eventual consistency through **event-driven orchestration**.

---

### 3. Order Placement & Validation

Authenticated users place orders via the **Order Service**. All endpoints in this service are secured using JWT-based authentication.

For **SELL orders**, the Order Service performs a **synchronous validation** by calling the Portfolio Service using a **Feign client** to ensure the user owns sufficient stock quantity. This call is protected using a **Resilience4j circuit breaker**, preventing cascading failures if the Portfolio Service is unavailable.

Once validation succeeds, the order is persisted in the Order Service’s database, and an **`order-placed`** event is published to Kafka.

---

### 4. Trade Matching & Execution

The **Trade Service** acts as the system’s **matching engine**. It consumes `order-placed` events from Kafka and maintains an **in-memory order book per stock symbol**, implemented using concurrent data structures.

For each stock:
- BUY orders and SELL orders are stored in separate queues
- Orders are matched using **price-time priority**
- A trade is executed when the highest buy price is greater than or equal to the lowest sell price

When a match occurs:
- Trade quantities are calculated
- Remaining quantities are re-queued if partially filled
- Two trade records (BUY and SELL) are created with a shared trade ID
- Trades are persisted in the Trade Service database
- A **`trade-placed`** event is published to Kafka

This design keeps the matching path **low-latency** and mirrors real exchange behavior, where order books live in memory and only executed trades are persisted.

---

### 5. Portfolio Updates from Trades

The **Portfolio Service** consumes `trade-placed` events to update user portfolios. Each trade triggers:
- Quantity adjustments for the corresponding stock
- Recalculation of weighted average price
- Updates to total portfolio value
- Removal of positions when quantity reaches zero

All updates are performed within **transactional boundaries** to ensure consistency. The Portfolio Service thus becomes the **source of truth for user holdings**, while remaining fully decoupled from order placement and trade execution.

---

## Service Discovery (Eureka)

The **Discovery Service** uses **Spring Cloud Eureka** to enable dynamic service registration and discovery. All microservices register themselves with Eureka at startup, allowing:
- Logical service naming
- Loose coupling between services
- Easier scaling and service replacement

Although most inter-service communication is event-driven, Eureka supports the synchronous Feign-based call from the Order Service to the Portfolio Service.

---

## Docker & Local Development

All services, along with Kafka and PostgreSQL, are orchestrated using **Docker Compose**, enabling a production-like local environment.

### Docker Compose includes:
- **Kafka (KRaft mode)** – No Zookeeper dependency
- **PostgreSQL 16** – Shared container with separate databases per service
- **Eureka Discovery Service**
- **User, Order, Trade, and Portfolio Services**

Each microservice:
- Has an optimized Dockerfile
- Exposes its own port
- Uses environment variables for database, Kafka, and Eureka configuration

### Running the project locally
```bash
docker compose up --build

Once started, you can view the Eureka Dashboard at http://localhost:8761

Each Microservice Readme is available inside the respective folders
