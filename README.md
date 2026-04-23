# 🚀 E-Commerce Backend System (Spring Boot + Kafka + Redis + Docker + CI/CD)

## 📌 Overview

This project is a **modern eCommerce backend system** built using Spring Boot.
It demonstrates real-world backend architecture including:

* REST APIs
* JWT Authentication
* Redis Caching
* Kafka Event-Driven Architecture
* Dockerized Deployment
* CI/CD with GitHub Actions

---

## 🏗️ Architecture

```
User → Auth (JWT)
     → Product Service
     → Cart Service
     → Order Service
            ↓
        Kafka Events
            ↓
   Payment Service (simulated)
            ↓
   Order Status Update
            ↓
   Email + Analytics Consumers
```

---

## 🛠️ Tech Stack

**Backend:**

* Java 21
* Spring Boot
* Spring Security (JWT)
* Spring Data JPA (Hibernate)

**Database:**

* PostgreSQL

**Caching:**

* Redis (Spring Cache + Token Blacklist)

**Messaging:**

* Apache Kafka (Event-driven architecture)

**DevOps:**

* Docker & Docker Compose
* GitHub Actions (CI/CD)

---

## 🔐 Authentication Flow

* User registers → stored in DB
* User logs in → JWT token generated
* Token used in API requests
* Logout → token stored in Redis blacklist

---

## ⚡ Key Features

### 👤 User Management

* Register / Login
* JWT Authentication
* Role-based access

---

### 📦 Product APIs

* Create product
* Get all products
* Get product by ID
* Update product
* Delete product
* Redis caching enabled

---

### 🛒 Cart System (Stateful)

* Add to cart
* Remove item
* View cart
* Clear cart
* Stored in DB + cached in Redis

---

### 📦 Order System

* Place order from cart
* Inventory update (product quantity reduced)
* Cart cleared after order

---

### 🔄 Event-Driven Flow (Kafka)

```
Order Created (PENDING)
        ↓
Payment Service (simulated)
        ↓
Payment Processed Event
        ↓
Order Status Updated → COMPLETED
        ↓
Email + Analytics Consumers triggered
```

---

## 🧠 Concepts Implemented

* DTO Pattern
* Cache Aside Pattern
* Token Blacklisting
* Idempotent APIs (DELETE operations)
* Eventual Consistency
* Event-Driven Architecture
* Microservices-style design (within single app)

---

## 🐳 Running the Project (Docker)

### ✅ Step 1: Go to project root

```
cd <project-root>
```

### ✅ Step 2: Start all services

```
docker-compose up -d
```

---

### 🚀 Services Started

* Spring Boot App → http://localhost:8080
* Kafka → localhost:9092
* Redis → localhost:6379
* Kafka UI → http://localhost:8085

---

## 🔑 Default Credentials

```
Username: user
Password: (check console logs on startup)
```

---

## 🧪 Sample API Flow

### 1️⃣ Register User

```
POST /auth/register
```

---

### 2️⃣ Login

```
POST /auth/login
```

👉 Copy JWT token

---

### 3️⃣ Create Product

```
POST /api/products
```

---

### 4️⃣ Add to Cart

```
POST /api/cart/{userId}
```

---

### 5️⃣ Place Order

```
POST /api/orders/{userId}
```

---

## 📊 Kafka Topics

* `user-registered`
* `order-created`
* `payment-processed`

---

## 🔄 CI/CD Pipeline

On every push to `main`:

1. Build project (Maven)
2. Create Docker image
3. Push image to Docker Hub

---

## 🧩 Future Enhancements

* React Frontend (in progress)
* API Gateway (Spring Cloud Gateway)
* Microservices split (User, Order, Product)
* Kubernetes deployment
* Monitoring (Prometheus + Grafana)
* Kafka Schema Registry (Avro)

---

## 🎯 Purpose

This project is built for:

* Self Learning and practice of modern backend development
* Demonstrating modern backend architecture
* Hands-on learning of distributed systems

---

## 👨‍💻 Author

Fahad Nizamani
