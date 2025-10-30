# 🏦 personal.bank

---

## 🚀 Technology Stack

* **Java 17**
* **Spring Boot 3** (Web, Security, Data JPA, Cache)
* **PostgreSQL**
* **Redis**
* **JWT** (authentication)
* **Swagger / OpenAPI**
* **Docker & Docker Compose**
* **Maven**

---

## 📋 Prerequisites

Before running or testing the project, make sure the following requirements are met:

### System Requirements

* **Java 17** installed (for local execution or running the JAR file)
* **Docker** and **Docker Compose** installed (for containerized deployment)
* **Git** installed (for repository cloning)

---

## 🔧 Cloning the Repository

```bash
git clone https://github.com/Andrey-Pominov/personal.bank.git
cd personal.bank
```

---

## ⚙️ Environment Configuration (.env)

Ensure there is a `.env` file in the project root with the required environment variables.
If it’s missing, create it manually using `.env.example` as a reference or the sample below:

```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=root
POSTGRES_DB=userbank
REDIS_HOST=redis
REDIS_PORT=6379
JWT_SECRET=your_jwt_secret_here
```

---

## 🗄️ Database Setup

The application uses **PostgreSQL**.
If you experience database connection issues during Docker Compose startup (e.g., authentication errors for the `postgres` user), you can create the user manually:

```bash
docker run -it --rm postgres psql -h localhost -U postgres
CREATE USER postgres WITH PASSWORD 'root';
CREATE DATABASE userbank;
GRANT ALL PRIVILEGES ON DATABASE userbank TO postgres;
```

---

## 👤 Predefined Test Users

Database migrations create three predefined test users:

| Email                                           | Phone       | Password (hashed) |
| ----------------------------------------------- | ----------- | ----------------- |
| [ivan@example.com](mailto:ivan@example.com)     | 79000000001 | hashedpassword123 |
| [elena@example.com](mailto:elena@example.com)   | 79000000002 | securepassword456 |
| [dmitry@example.com](mailto:dmitry@example.com) | 79000000003 | pass789hash       |

---

## 🐳 Running with Docker Compose

Build and start the containers:

```bash
docker-compose up --build
```

This command creates and launches **three containers**:

* Application
* PostgreSQL
* Redis

If the application fails to connect to the database, double-check your `.env` file and ensure that the PostgreSQL credentials are correct.

---

## ✅ Verifying Running Containers

```bash
docker ps
```

Make sure that all three containers (application, PostgreSQL, Redis) are up and running.

---

## 🌐 API Documentation

Once the containers are running, open the Swagger UI in your browser:

👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

**personal.bank** is a modular, containerized backend project designed for secure user management, caching, and authentication, following modern Spring Boot and microservice best practices.
