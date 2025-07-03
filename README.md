# 🏦 personal.bank

personal.bank — это микросервис для управления пользователями, их аккаунтами, контактной информацией и переводами средств между ними. Поддерживает авторизацию через email или телефон, REST API, кэширование, расчёт процентов и трансферы.

---

## 🚀 Стек технологий

- Java 17
- Spring Boot 3 (Web, Security, Data JPA, Cache)
- PostgreSQL
- Redis
- JWT (аутентификация)
- Swagger (OpenAPI)
- Docker + Docker Compose
- Maven

---

## 📦 Быстрый старт

### 1. Клонирование репозитория

```bash
git clone https://github.com/Andrey-Pominov/personal.bank.git
cd personal.bank
```

#  Работа с Liquibase
Можно сгенерировать автоматически:
- Liquibase может создать changelog на основе уже существующей БД:
 
```liquibase generateChangeLog --url=jdbc:postgresql://localhost:5432/{dbname} --username={user} --password={pass} --outputFile=db/changelog.xml ```
- Сравнить текущее состояние БД с моделью и создать новую миграцию:

```liquibase diffChangeLog --referenceUrl=jdbc:postgresql://localhost:5432/{dbname} --username={user} --password={pass} --outputFile=db/new_changes.xml ```

- Spring Boot может сам генерировать changelog на основе @Entity классов, если настроить ```spring.jpa.hibernate.ddl-auto=update```, но это плохая практика для продакшена.