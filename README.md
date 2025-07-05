# 🏦 personal.bank

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

## 📋 Предварительные требования
Перед началом тестирования убедитесь, что выполнены следующие условия:

Требования к системе:
Установлен Java 17 (для локального запуска или работы с JAR-файлом).
Установлены Docker и Docker Compose (для запуска в контейнерах).
Установлен Git для клонирования репозитория.

Клонирование репозитория:
bash
````
git clone https://github.com/Andrey-Pominov/personal.bank.git
cd personal.bank
````
### Файл окружения (.env):

Убедитесь, что в корне проекта есть файл .env с необходимыми настройками (например, учетные данные базы данных, Redis, секретный ключ JWT). Если файл отсутствует, создайте его на основе .env.example или используйте пример:
env

````
POSTGRES_USER=postgres
POSTGRES_PASSWORD=root
POSTGRES_DB=userbank
REDIS_HOST=redis
REDIS_PORT=6379
JWT_SECRET=your_jwt_secret_here
````
### Настройка базы данных:
Приложение использует PostgreSQL. Если при запуске Docker Compose возникают проблемы с подключением к базе данных (например, пользователь postgres с паролем root не найден), создайте пользователя вручную:
bash
````
docker run -it --rm postgres psql -h localhost -U postgres
CREATE USER postgres WITH PASSWORD 'root';
CREATE DATABASE userbank;
GRANT ALL PRIVILEGES ON DATABASE userbank TO postgres;
````
### Предустановленные пользователи: 
Миграция базы данных создает трех тестовых пользователей:
````
Пользователь 1: Email: ivan@example.com, Телефон: 79000000001, Пароль: hashedpassword123
Пользователь 2: Email: elena@example.com, Телефон: 79000000002, Пароль: securepassword456
Пользователь 3: Email: dmitry@example.com, Телефон: 79000000003, Пароль: pass789hash
````
## Запуск с Docker Compose
Сборка и запуск контейнеров:
bash
````
docker-compose up --build
````
Команда создает и запускает три контейнера: приложение, PostgreSQL и Redis.
Если возникают проблемы с базой данных, проверьте файл .env и убедитесь, что пользователь и пароль PostgreSQL совпадают.
Проверка контейнеров:
bash
````
docker ps
````
Убедитесь, что все три контейнера (приложение, PostgreSQL, Redis) запущены.
Доступ к Swagger UI:
Откройте в браузере http://localhost:8080/swagger-ui/index.html.
