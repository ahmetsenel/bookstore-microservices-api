# 📚 Bookstore Microservices
 
> A production-inspired bookstore backend built with **Spring Boot** and **Spring Cloud**, covering real-world microservices patterns including service discovery, API gateway routing, inter-service communication, event-driven architecture, role-based access control, and email notifications.
 
---
 
## 📌 Table of Contents
 
- [✨ Features](#-features)
- [🛠️ Tech Stack](#-tech-stack)
- [📁 Project Structure](#-project-structure)
- [🏗️ Architecture](#%EF%B8%8F-architecture)
- [🔁 Order Flow](#-order-flow)
- [📦 Modules](#-modules)
- [🔐 Security](#-security)
- [📡 API Endpoints](#-api-endpoints)
- [📨 Kafka Topics](#-kafka-topics)
- [⚙️ Environment Variables](#%EF%B8%8F-environment-variables)
- [🐳 Running with Docker](#-running-with-docker)
- [📄 Swagger / API Docs](#-swagger--api-docs)
---
 
## ✨ Features
 
- 🧩 **Microservices architecture** — independently deployable services, each with its own database schema
- 🔍 **Service discovery** — all services register with Eureka; the gateway resolves them dynamically
- 🚪 **API Gateway** — single entry point with JWT validation filter; routes requests to downstream services
- 🔐 **JWT-based stateless auth** — tokens issued by `user-service`, validated at the gateway and at each service via OAuth2 Resource Server
- 👮 **Role-based access control** — `ROLE_USER` and `ROLE_ADMIN` enforced at both gateway and service level using `@PreAuthorize` and `hasRole()`
- 🔗 **OpenFeign + Load Balancer** — type-safe, declarative HTTP clients for inter-service calls with Eureka-based load balancing
- 📬 **Event-driven messaging with Kafka** — order creation triggers a stock reservation flow; result triggers an email notification
- 🔄 **Transactional outbox pattern** — Kafka messages are sent only `afterCommit()` using `TransactionSynchronizationManager`, preventing phantom events on rollback
- 📧 **HTML email notifications** — `notification-service` sends styled order confirmation emails via Spring Mail
- 🗂️ **`common-lib`** — shared events, DTOs, response wrappers, and exception types used across all services
- 🔒 **`common-security`** — reusable JWT utilities (`JwtUtil`) and security handlers (`AccessDeniedHandlerImpl`) shared across services
- 📖 **Centralized Swagger UI** — all service API docs aggregated at the gateway via SpringDoc
- 🐳 **Docker Compose ready** — full stack runs with a single command; KRaft-mode Kafka (no Zookeeper needed)
- 🧱 **Multi-module Maven** — single parent POM manages all versioning; no repeated dependency declarations across modules
---

<a id="-tech-stack"></a>
## 🛠️ Tech Stack
 
| 🔧 Layer | 🚀 Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.6 |
| Service Discovery | Spring Cloud Netflix Eureka |
| API Gateway | Spring Cloud Gateway (WebFlux / Reactive) |
| Inter-service Comm | OpenFeign + Spring Cloud LoadBalancer |
| Messaging | Apache Kafka (KRaft mode — no Zookeeper) |
| Security | JWT (JJWT 0.11.5), OAuth2 Resource Server |
| Persistence | Spring Data JPA + PostgreSQL 16 |
| DTO Mapping | MapStruct 1.6.3 |
| Shared Libraries | `common-lib` (DTOs, events, exceptions), `common-security` (JWT, handlers) |
| Email | Spring Boot Mail (JavaMailSender + MimeMessage) |
| API Docs | SpringDoc OpenAPI + centralized Swagger UI at gateway |
| Build Tool | Maven (multi-module with parent POM) |
| Containerization | Docker + Docker Compose |
 
---

## 📁 Project Structure
 
```
│
├── 📦 common-lib/              ← Shared DTOs, events, exceptions, response wrappers
├── 🔒 common-security/         ← JWT utilities, auth converters, security handlers
├── 🔭 eureka-server/           ← Service registry
├── 🚪 api-gateway/             ← Single entry point, JWT validation, Swagger aggregation
├── 👤 user-service/            ← Registration, login, JWT issuance, user CRUD
├── 📖 book-service/            ← Book catalog, stock management, Kafka consumer
├── 🛒 order-service/           ← Order lifecycle, Feign clients, Kafka producer/consumer
├── 📧 notification-service/    ← Kafka consumer, HTML email sender
└── 📄 pom.xml                  ← Parent POM (multi-module, centralized versions)
```

---

## 🏗️ Architecture
 
```
                     ┌──────────────────────────────────┐
                     │          Eureka Server           │
                     │      Service Registry :8761      │
                     └──────────────┬───────────────────┘
                                    │ ← all services register here
          ┌─────────────────────────┼──────────────────────────┐
          │                         │                          │
          ▼                         ▼                          ▼
 ┌─────────────────┐     ┌──────────────────┐      ┌──────────────────┐
 │  User Service   │     │  Book Service    │      │  Order Service   │
 │  :8081          │     │  :8082           │      │  :8083           │
 │                 │     │                  │      │                  │
 │ • Register      │     │ • Book CRUD      │      │ • Create orders  │
 │ • Login → JWT   │     │ • Stock mgmt     │      │ • Feign → Book   │
 │ • User CRUD     │     │ • Kafka consumer │      │ • Feign → User   │
 │ • /internal/**  │     │ • /internal/**   │      │ • Kafka producer │
 └─────────────────┘     └──────────────────┘      └──────────────────┘
          ▲                         ▲                          │
          │ Feign (internal)        │ Feign (internal)         │
          └─────────────────────────┴──── Order Service ───────┘
                                                               │
                                                      Kafka: order-created
                                                               │
                                                               ▼
                                                    ┌──────────────────────┐
                                                    │   Book Service       │
                                                    │   (Kafka Consumer)   │
                                                    │   decreases stock    │
                                                    │ → Kafka: stock-result│
                                                    └──────────┬───────────┘
                                                               │
                                                  ┌────────────▼─────────────┐
                                                  │    Order Service         │
                                                  │    (Kafka Consumer)      │
                                                  │    updates order status  │
                                                  │  → Kafka: order-completed│
                                                  └──────────────────────────┘
                                                               │
                                                  ┌────────────▼─────────────┐
                                                  │    Notification Service  │
                                                  │    :8084                 │
                                                  │    sends HTML email      │
                                                  └──────────────────────────┘
 
🌐 Client ──► API Gateway :8080 ──► routes to services above
                    │
                    └── JWT validation on all routes except /api/auth/**, /api/books GET, /swagger/**
```
 
---
 
## 🔁 Order Flow
 
This is the core event-driven flow that ties the system together:
 
```
👤 User
  │
  │  POST /api/orders  (JWT required)
  ▼
🚪 API Gateway  ──── validates JWT ────►  🛒 Order Service
                                                │
                                                │ 1. Extracts userId from JWT
                                                │ 2. Calls BookClient (Feign) → book-service/internal/books/{id}
                                                │ 3. Calculates totalPrice
                                                │ 4. Saves Order (status: PENDING)
                                                │ 5. afterCommit() → publishes to Kafka
                                                │
                                         topic: order-created
                                                │
                                                ▼
                                         📦 Book Service (consumer)
                                                │
                                                │ 6. Validates stock for each item
                                                │ 7. Decreases stock transactionally
                                                │ 8. afterCommit() → publishes result
                                                │
                                         topic: stock-result
                                                │
                                    ┌───────────▼────────────┐
                                    │                        │
                                    │                        │
                              ✅ SUCCESS                ❌ FAIL
                                    │                        │
                                    ▼                        ▼
                             🛒 Order Service         🛒 Order Service
                             status → COMPLETED       status → FAILED
                                    │
                                    │ 9. Calls UserClient (Feign) → user-service/internal/users/{id}
                                    │ 10. Builds NotificationEvent with order items & prices
                                    │ 11. afterCommit() → publishes to Kafka
                                    │
                             topic: order-completed
                                    │
                                    ▼
                             📧 Notification Service
                                    │
                                    │ 12. Sends styled HTML email to user
                                    ▼
                             ✉️ User's inbox
```
 
---
 
## 📦 Modules
 
### 🏛️ `common-lib`
Shared domain library installed into local Maven repo. Contains all cross-service DTOs, events, exceptions, and response utilities.
 
| 📂 Package | 📄 Contents |
|---|---|
| `common.exception` | `BaseException`, `MessageType` (enum with HTTP status codes & messages) |
| `common.response` | `ApiResponse<T>`, `ErrorResponse`, `PageResponse<T>`, `ResponseUtil` |
| `common.event` | `OrderCreatedEvent`, `StockResultEvent`, `NotificationEvent` |
| `common.dto` | `OrderRequest`, `OrderItemRequest`, `ItemInfo`, `FieldValidationError`, `PaginationRequest` |
 
---
 
### 🔒 `common-security`
Reusable security components. Imported by `user-service`, `book-service`, and `order-service` to avoid duplicating JWT and security handler logic.
 
| 📂 Package | 📄 Contents |
|---|---|
| `security.jwt` | `JwtUtil` — token generation & claim extraction; `JwtAuthConverter` — extracts `role` claim into `GrantedAuthority`; `JwtDecoderFactory` — builds `NimbusJwtDecoder` from Base64 secret |
| `security.handler` | `AccessDeniedHandlerImpl` — returns structured `403 ErrorResponse` JSON |
 
---
 
### 🔭 `eureka-server` — `:8761`
Central service registry. All microservices register on startup. The API Gateway discovers service URLs dynamically.
 
---
 
### 🚪 `api-gateway` — `:8080`
Reactive gateway built on Spring Cloud Gateway (WebFlux). Single entry point for all client traffic.
 
**Routing table:**
 
| 🛤️ Path Pattern | 🎯 Target Service |
|---|---|
| `/api/auth/**`, `/api/users/**`, `/api/admin/users/**` | `USER-SERVICE` |
| `/api/books/**`, `/api/admin/books/**` | `BOOK-SERVICE` |
| `/api/orders/**`, `/api/admin/orders/**` | `ORDER-SERVICE` |
| `/user-service/v3/api-docs/**` | `USER-SERVICE` (Swagger) |
| `/book-service/v3/api-docs/**` | `BOOK-SERVICE` (Swagger) |
| `/order-service/v3/api-docs/**` | `ORDER-SERVICE` (Swagger) |
 
**Security at gateway:**
- JWT decoded using `NimbusReactiveJwtDecoder` with HMAC-SHA256
- Public: `/api/auth/**`, `GET /api/books/**`, `/swagger-ui/**`, `/v3/api-docs/**`
- Everything else requires a valid token
---
 
### 👤 `user-service` — `:8081`
Handles registration, authentication, and user management.
 
**Key classes:**
- `AuthService` — registers users (BCrypt password encoding), issues JWT tokens with `userId`, `email`, `role` claims
- `UserService` — CRUD for user profiles
- `SecurityConfig` — OAuth2 Resource Server; `ROLE_ADMIN` for `/api/admin/**`, `ROLE_USER` for `/api/users/**`
- `GlobalExceptionHandler` — handles `BaseException` and `MethodArgumentNotValidException` with structured `ErrorResponse`
- `InternalController` — exposes `/internal/users/{id}` for Feign calls from `order-service` (no auth required)
---
 
### 📖 `book-service` — `:8082`
Manages the book catalog and stock.
 
**Key classes:**
- `BookService` — CRUD, paginated listing, stock management, Kafka event handling
- `handleOrder()` — idempotent stock deduction; uses `processedOrders` set to skip duplicate Kafka messages; registers `afterCommit()` hook to send `stock-result` only after successful DB commit
- `OrderConsumer` — listens to `order-created` Kafka topic
- `StockResultProducer` — publishes success/failure to `stock-result` topic
- `InternalBookController` — `/internal/books/{id}` for Feign (no auth)
- `AdminBookController` — `/api/admin/books/**` requires `ROLE_ADMIN`
---
 
### 🛒 `order-service` — `:8083`
Creates and manages orders. Orchestrates the stock check + notification flow.
 
**Key classes:**
- `OrderService.createOrder()` — fetches book prices via Feign, calculates total, saves order as `PENDING`, publishes `order-created` after commit
- `OrderService.updateStatus()` — consumes `stock-result`; on success → sets `COMPLETED`, fetches user info, builds `NotificationEvent`, publishes `order-completed` after commit; on failure → sets `FAILED`
- `BookClient` / `UserClient` — Feign clients calling `/internal/**` endpoints
- `BookListener` — listens to `stock-result`
- `StockProducer` / `NotificationProducer` — Kafka producers
---
 
### 📧 `notification-service` — `:8084`
Stateless service; no database. Purely event-driven.
 
**Key classes:**
- `OrderListener` — listens to `order-completed` Kafka topic
- `NotificationService.sendHtmlMail()` — builds and sends a styled HTML email with order ID, item list (quantity × title × price), and total price
---
 
## 🔐 Security
 
The project implements a **layered JWT security model**:
 
### 🔑 Layer 1 — Token Issuance (`user-service`)
- `POST /api/auth/login` returns a signed JWT
- Signed with HMAC-SHA256 using a shared Base64-encoded secret
- Token claims: `userId` (Long), `email` (String), `role` (String, e.g. `ROLE_USER`)
- Token expiry: configurable via `JwtUtil`
### 🛡️ Layer 2 — Gateway Validation (`api-gateway`)
- `NimbusReactiveJwtDecoder` validates the signature on every request
- Public paths bypass the filter
- Invalid/expired tokens → `401 Unauthorized` via `AuthenticationEntryPointImpl`
### 🔒 Layer 3 — Resource Server (`user-service`, `book-service`, `order-service`)
- Each service is an independent OAuth2 Resource Server
- Uses `JwtDecoderFactory` (from `common-lib`) to decode with the shared secret
- `JwtAuthConverter` extracts the `role` claim and builds `SimpleGrantedAuthority`
- Role enforcement:

| 🔑 Role | 🛤️ Accessible Paths |
|---|---|
|👤`ROLE_USER` | `/api/users/**`, `/api/orders/**`, `GET /api/books/**` |
|👮`ROLE_ADMIN` | `/api/admin/**` (all services) |
|🌍 Anonymous | `/api/auth/**`, `GET /api/books/**`, `/swagger-ui/**` |
 
### 🚫 Error Responses
- **401 Unauthorized** → `AuthEntryPointImpl` — `"Invalid username or password."`
- **403 Forbidden** → `AccessDeniedHandlerImpl` — `"You do not have permission to access this resource"`
Both return a structured `ErrorResponse` JSON:
```json
{
  "timestamp": "2025-05-12T19:20:22",
  "status": 403,
  "error": "ACCESS_DENIED",
  "message": "You do not have permission to access this resource",
  "path": "/api/admin/users"
}
```
 
---
 
## 📡 API Endpoints
 
> All requests go through the **API Gateway** at `http://localhost:8080`
 
### 🔓 Auth — `user-service`
 
| ⚡ Method | 🛤️ Endpoint | 📝 Description | 🔑 Auth |
|---|---|---|---|
| `POST` | `/api/auth/register` | Register a new user | 🌍 Public |
| `POST` | `/api/auth/login` | Login → returns JWT | 🌍 Public |
 
**Register / Login Request:**
```json
{
  "username": "ahmet",
  "password": "password"
}
```
 
**Login Response:**
```json
{
  "success": true,
  "message": "Success",
  "timestamp": "2025-05-12T19:00:00",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```
 
---
 
### 👤 Users — `user-service`
 
| ⚡ Method | 🛤️ Endpoint | 📝 Description | 🔑 Auth |
|---|---|---|---|
| `GET` | `/api/users/me` | Get my profile | 👤 `ROLE_USER` |
| `PATCH` | `/api/users` | Update my profile | 👤 `ROLE_USER` |
| `GET` | `/api/admin/users` | List all users | 👮 `ROLE_ADMIN` |
| `GET` | `/api/admin/users/{id}` | Get user by ID | 👮 `ROLE_ADMIN` |
| `PATCH` | `/api/admin/users/{id}` | Update user by ID | 👮 `ROLE_ADMIN` |
| `DELETE` | `/api/admin/users/{id}` | Delete user | 👮 `ROLE_ADMIN` |
 
---
 
### 📖 Books — `book-service`
 
| ⚡ Method | 🛤️ Endpoint | 📝 Description | 🔑 Auth |
|---|---|---|---|
| `GET` | `/api/books` | List books (paginated) | 🌍 Public |
| `GET` | `/api/books/{id}` | Get book detail | 🌍 Public |
| `POST` | `/api/admin/books` | Create a book | 👮 `ROLE_ADMIN` |
| `PATCH` | `/api/admin/books/{id}/stock` | Update stock | 👮 `ROLE_ADMIN` |
| `DELETE` | `/api/admin/books/{id}` | Delete a book | 👮 `ROLE_ADMIN` |
 
**Pagination query params:** `?page=0&size=10`
 
---
 
### 🛒 Orders — `order-service`
 
| ⚡ Method | 🛤️ Endpoint | 📝 Description | 🔑 Auth |
|---|---|---|---|
| `POST` | `/api/orders` | Create an order | 👤 `ROLE_USER` |
| `GET` | `/api/orders/me` | Get my orders | 👤 `ROLE_USER` |
| `GET` | `/api/admin/orders/{id}` | Get order by ID | 👮 `ROLE_ADMIN` |
| `GET` | `/api/admin/orders/user/{userId}` | Get orders by user | 👮 `ROLE_ADMIN` |
| `DELETE` | `/api/admin/orders/{id}` | Delete order | 👮 `ROLE_ADMIN` |
 
**Create Order Request:**
```json
{
  "items": [
    { "bookId": 1, "quantity": 2 },
    { "bookId": 3, "quantity": 1 }
  ]
}
```
 
**Create Order Response:**
```json
{
  "success": true,
  "message": "Success",
  "timestamp": "2025-05-12T19:00:00",
  "data": {
    "id": 10
    "userId": 42,
    "status": "PENDING",
    "totalPrice": 149.97
    "items": [
       {
          "id": 1,
          "bookId": 1,
          "quantity": 2,
          "price": 50
       },
       {
          "id": 2,
          "bookId": 3,
          "quantity": 1,
          "price": 100
       }
    ]
  }
}
```
 
---
 
## 📨 Kafka Topics
 
| 📬 Topic | 📤 Producer | 📥 Consumer | 📦 Payload |
|---|---|---|---|
| `order-created` | `order-service` | `book-service` | `OrderCreatedEvent` (orderId, items) |
| `stock-result` | `book-service` | `order-service` | `StockResultEvent` (orderId, success, message) |
| `order-completed` | `order-service` | `notification-service` | `NotificationEvent` (orderId, email, items, totalPrice) |
 
> ⚠️ Kafka runs in **KRaft mode** (no Zookeeper) using `confluentinc/cp-kafka:7.5.0`

---
 
 
## ⚙️ Environment Variables
 
| ⚙️ Variable | 📝 Description | 💡 Example |
|---|---|---|
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5433/bookstore` |
| `SPRING_DATASOURCE_USERNAME` | DB username | `admin` |
| `SPRING_DATASOURCE_PASSWORD` | DB password | `admin` |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | Kafka broker | `localhost:9092` |
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | Eureka URL | `http://localhost:8761/eureka` |
| `JWT_SECRET` | Base64 HMAC-SHA256 key (min 256-bit) | `eLMcSMb6bh8xsOVJTu0mqSE11gdmjNDcEYn4NrrFpqY=` |
| `MAIL_USERNAME` | SMTP username | `your@gmail.com` |
| `MAIL_PASSWORD` | SMTP app password | `abcd efgh ijkl mnop` |
 
 
---
 
## 🐳 Running with Docker
 
The easiest way to run the full stack.
 
### 📋 Prerequisites
- Docker Desktop (or Docker Engine + Compose)
### 🚀 Steps
 
**1. Create a `.env` file** in the project root:
```env
MAIL_USERNAME=your@gmail.com
MAIL_PASSWORD=your_app_password
```
> ⚠️ **Gmail users:** `MAIL_PASSWORD` is **not** your regular Gmail password. You must generate an **App Password** from your Google account (`Google Account → Security → 2-Step Verification → App Passwords`). Regular passwords will be rejected by Gmail's SMTP server.
 
**2. Pull and start everything:**
```bash
docker compose up -d
```
 
**3. Verify all services are up:**
 
| 🔗 URL | 📝 What to check |
|---|---|
| `http://localhost:8761` | Eureka dashboard — all services registered |
| `http://localhost:8080/swagger-ui.html` | Centralized Swagger UI |
 
> 💡 Services depend on `postgres` and `kafka` health checks, so startup order is handled automatically.
 
**4. Stop everything:**
```bash
docker compose down
```

---
 
## 📄 Swagger / API Docs
 
All service APIs are aggregated in a **single Swagger UI** served at the gateway:
 
🔗 **`http://localhost:8080/swagger-ui.html`**
 
Available API groups:
 
| 🗂️ Group | 📡 Source URL |
|---|---|
| `user-service` | `/user-service/v3/api-docs` |
| `book-service` | `/book-service/v3/api-docs` |
| `order-service` | `/order-service/v3/api-docs` |

 
---

## 👨‍💻 Author
 
Made with ❤️ by **Ahmet Şenel**
 
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?style=flat&logo=linkedin)](https://linkedin.com/in/ahmetşenel)
[![GitHub](https://img.shields.io/badge/GitHub-Follow-black?style=flat&logo=github)](https://github.com/ahmetsenel)

---
