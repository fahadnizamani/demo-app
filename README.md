# 🚀 E-Commerce Microservices Backend

A hands-on e-commerce backend project built with **Java 21, Spring Boot, Spring Cloud, PostgreSQL, Redis, Kafka, Docker, and Kubernetes**.

The project is focused on learning and demonstrating:

- Spring Boot and Spring Security
- JWT authentication and authorization
- REST APIs and microservices
- Redis caching and token blacklisting
- Apache Kafka and event-driven architecture
- Docker and Docker Compose
- Spring Cloud API Gateway and Eureka
- PostgreSQL / JPA / Hibernate
- Observability with Prometheus, Grafana, and Jaeger
- Kubernetes fundamentals

---

## 🏗️ Current Architecture

The project can be run in different ways. The current day-to-day setup is **Docker + IntelliJ**.

```text
                         Client / Postman
                                |
                                v
                    +----------------------+
                    | API Gateway :8081   |
                    |       Docker         |
                    +----------+-----------+
                               |
             +-----------------+------------------+
             |                 |                  |
             v                 v                  v
      Demo/App :8080    Product :8082      Order :8084
         Docker            IntelliJ          IntelliJ
                                                |
                                                v
                                         Payment :8083
                                            IntelliJ

       +------------------------------------------------+
       | Docker infrastructure                           |
       | Kafka :9092     Redis :6379     Kafka UI :8085 |
       | Zookeeper :2181                                |
       +------------------------------------------------+

                       PostgreSQL :5432
                         Windows host
```

---

# 🛠️ Technology Stack

## Backend

- Java 21
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA / Hibernate
- Spring Cloud Gateway
- Eureka Service Registry

## Database

- PostgreSQL

## Caching

- Redis
- Spring Cache
- JWT / token blacklist

## Messaging

- Apache Kafka
- Zookeeper
- Kafka UI

## Containers

- Docker
- Docker Compose

## Observability

- Prometheus
- Grafana
- Jaeger
- OpenTelemetry / Micrometer Tracing

## Frontend

- React frontend project is included in the repository.

## CI/CD

- GitHub Actions
- Docker image build and push

## Kubernetes

Kubernetes manifests are kept in the repository for learning and reference.

The current day-to-day setup uses **Docker Compose + IntelliJ** instead of Kubernetes.

---

# 📁 Project Structure

```text
workspace-demo/
│
├── api-gateway/
├── demo/
│   └── docker-compose.yml
├── ecommerce-frontend/
├── order-service/
├── payment-service/
├── product-service/
├── service-registry/
│
├── k8s/
│   ├── api-gateway-deployment.yml
│   ├── demo-app-deployment.yml
│   ├── kafka-deployment.yml
│   ├── kafka-ui-deployment.yml
│   └── redis-deployment.yml
│
├── monitoring/
│   └── prometheus.yml
│
└── README.md
```

---

# 🐳 Running the Application

## 1. Start Docker infrastructure

The Docker Compose file is located under the `demo` directory.

From the repository root:

```bash
cd demo
docker compose up -d
```

Or directly from the repository root:

```bash
docker compose -f demo/docker-compose.yml up -d
```

Check the containers:

```bash
docker ps
```

Expected containers include:

```text
zookeeper
kafka
kafka-ui
redis
demo-app
api-gateway
```

### Docker Compose warning

If Docker reports that the `version` attribute is obsolete, remove this line from `demo/docker-compose.yml`:

```yaml
version: '3.8'
```

Modern Docker Compose no longer requires it.

---

# 🔌 Ports

| Component | Port | Access |
|---|---:|---|
| Demo/App | 8080 | `http://localhost:8080` |
| API Gateway | 8081 | `http://localhost:8081` |
| Product Service | 8082 | IntelliJ / local |
| Payment Service | 8083 | IntelliJ / local |
| Order Service | 8084 | IntelliJ / local |
| Kafka UI | 8085 | `http://localhost:8085` |
| Kafka | 9092 | `localhost:9092` |
| Redis | 6379 | `localhost:6379` |
| PostgreSQL | 5432 | `localhost:5432` |
| Eureka | 8761 | `http://localhost:8761` |
| Prometheus | 9090 | `http://localhost:9090` |
| Grafana | 3000 | `http://localhost:3000` |
| Jaeger | 16686 | `http://localhost:16686` |
| Jaeger OTLP HTTP | 4318 | `localhost:4318` |

---

# 🧩 Docker Networking

A key Docker networking concept in this project is the difference between container-to-container communication and container-to-host communication.

## Container → Container

Docker containers communicate using Docker Compose service names.

Examples:

```text
kafka:29092
redis:6379
app:8080
```

Do not use `localhost` to reach another container.

For example, from the API Gateway container:

```text
http://app:8080
```

is correct.

```text
http://localhost:8080
```

would point back to the API Gateway container itself.

## Container → Windows host

For applications running directly on Windows, Docker Desktop provides:

```text
host.docker.internal
```

Examples used by this project:

```text
jdbc:postgresql://host.docker.internal:5432/demo_db
http://host.docker.internal:8082
```

---

# 🖥️ Start IntelliJ Services

After Docker infrastructure is running, start the required Spring Boot applications from IntelliJ.

Typical local services:

```text
Service Registry / Eureka : 8761
Product Service           : 8082
Payment Service           : 8083
Order Service             : 8084
```

The Dockerized API Gateway communicates with locally running services through `host.docker.internal`.

---

# 📊 Monitoring Stack

Prometheus, Grafana, and Jaeger are currently separate Docker containers rather than services in `demo/docker-compose.yml`.

The existing containers are named:

```text
prometheus
grafana
jaeger
```

## Start existing monitoring containers

If these containers already exist but are stopped:

```bash
docker start prometheus grafana jaeger
```

Check:

```bash
docker ps
```

Open:

```text
Prometheus: http://localhost:9090
Grafana:    http://localhost:3000
Jaeger:     http://localhost:16686
```

Jaeger OTLP HTTP endpoint:

```text
http://localhost:4318
```

The Order Service tracing configuration uses:

```text
http://localhost:4318/v1/traces
```

## Stop monitoring containers

```bash
docker stop prometheus grafana jaeger
```

> These commands start the existing monitoring containers. If the containers are deleted and need to be recreated, retain their original Docker configuration and volume mappings, especially the Prometheus configuration using `monitoring/prometheus.yml`.

---

# 🔎 Observability

Spring Boot Actuator exposes endpoints such as:

```text
/actuator/health
/actuator/info
/actuator/prometheus
```

## Metrics flow

```text
Spring Boot Service
       |
       | metrics
       v
  Prometheus
       |
       v
    Grafana
```

## Tracing flow

```text
Spring Boot Service
       |
       | traces / OTLP
       v
     Jaeger
```

---

# 🔐 Authentication Flow

The application uses JWT-based authentication.

```text
Register
   |
   v
User stored in PostgreSQL
   |
   v
Login
   |
   v
JWT generated
   |
   v
Authorization: Bearer <JWT>
   |
   v
API Gateway / Security
   |
   v
Protected API
```

Logout uses Redis for token blacklisting.

---

# 🧪 API Testing

The main APIs can be tested through the API Gateway:

```text
http://localhost:8081
```

## Login

```http
POST http://localhost:8081/auth/login
```

After successful login, copy the JWT and use it for protected requests:

```text
Authorization: Bearer <JWT>
```

## Products

```http
GET http://localhost:8081/api/products
```

## Cart

```http
GET http://localhost:8081/api/cart
```

## Orders

```http
GET http://localhost:8081/api/orders
```

> Check the current controller/API implementation for the exact HTTP method, path parameters, request body, and authorization requirements.

---

# 🔄 Order / Kafka Flow

A simplified event-driven order flow:

```text
Client
  |
  v
API Gateway
  |
  v
Order Service
  |
  +----> PostgreSQL
  |
  +----> Kafka: order-created
                |
                v
         Payment Service
                |
                v
       payment-processed
                |
                v
         Order Service
                |
                v
        Order status update
```

Kafka UI:

```text
http://localhost:8085
```

---

# 🗄️ PostgreSQL

Current local configuration:

```text
Host:     localhost
Port:     5432
Database: demo_db
Username: postgres
```

Docker containers access the Windows-host PostgreSQL instance through:

```text
host.docker.internal:5432
```

---

# ⚡ Redis

Redis runs in Docker:

```text
localhost:6379
```

From another Docker container:

```text
redis:6379
```

Redis is used for caching and token blacklist functionality.

A Redis GUI can connect to:

```text
localhost:6379
```

when the Redis container is running.

---

# ☸️ Kubernetes

The repository contains Kubernetes manifests under:

```text
k8s/
```

These files are intentionally kept in Git as part of the project's Kubernetes learning material.

Current manifests include:

```text
api-gateway-deployment.yml
demo-app-deployment.yml
kafka-deployment.yml
kafka-ui-deployment.yml
redis-deployment.yml
```

The Kubernetes work covered concepts including:

- Pods
- Deployments
- Services
- Nodes
- NodePort
- Multiple pod replicas
- Service-based load balancing
- Horizontal scaling concepts
- Port forwarding

The current project does not require Kubernetes for the normal Docker + IntelliJ development setup.

---

# 🧠 Concepts Demonstrated

- REST APIs
- DTO pattern
- JWT authentication
- Role-based authorization
- Spring Security
- Redis caching
- Cache-aside pattern
- Token blacklisting
- PostgreSQL
- JPA / Hibernate
- Microservices architecture
- API Gateway
- Service discovery / Eureka
- Kafka
- Event-driven architecture
- Asynchronous processing
- Eventual consistency
- Idempotent operations
- Docker
- Docker Compose
- Docker networking
- Kubernetes fundamentals
- Prometheus metrics
- Grafana dashboards
- Distributed tracing with Jaeger
- OpenTelemetry / Micrometer
- CI/CD

---

# 🔄 CI/CD

The project uses GitHub Actions for CI/CD.

General pipeline:

```text
Git Push
   |
   v
GitHub Actions
   |
   +--> Build / Test
   |
   +--> Build Docker Image
   |
   +--> Push image to Docker Hub
```

---

# 🧹 Stopping the Application

## Stop Docker Compose

From `demo`:

```bash
docker compose down
```

Or from the repository root:

```bash
docker compose -f demo/docker-compose.yml down
```

## Stop monitoring

```bash
docker stop prometheus grafana jaeger
```

## Stop IntelliJ services

Stop the Spring Boot applications from IntelliJ.

---

# 🎯 Project Purpose

This project is a hands-on learning project for understanding modern backend and distributed-system architecture.

The goal is to understand how a backend system behaves when it includes:

```text
Authentication
     +
Microservices
     +
API Gateway
     +
Database
     +
Caching
     +
Messaging
     +
Distributed processing
     +
Observability
     +
Containers
     +
Kubernetes
```

The project provides practical exposure to building, running, connecting, monitoring, and troubleshooting these components together.

---

## 👨‍💻 Author

**Fahad Nizamani**
