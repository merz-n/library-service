
# Library Service

A Spring Boot REST microservice to manage **books**, **authors**, **users**, and the **borrow/return** flow in PostgreSQL.
Built with Java 21, Spring Boot 3, JPA (Hibernate), Flyway, and Swagger UI.

---

## Features

- Manage **authors**, **books**, and **users** (create, read; optional update/delete)
- **Borrow** and **return** a book
- List **available** books
- Basic search: by author, availability, email
- API documentation via **Swagger UI**

---

## Tech Stack

- Java 21, Maven
- Spring Boot 3 (Web, Data JPA, Validation)
- PostgreSQL + **Flyway** (automatic DB migrations)
- springdoc-openapi (**Swagger UI**)

---

## Database Setup

1. Start PostgreSQL locally (default port **5432**).
2. Create a database and user (defaults used by the app):

```sql
CREATE DATABASE library_service_2025;

CREATE USER admin WITH PASSWORD 'admin';
GRANT ALL PRIVILEGES ON DATABASE library_service_2025 TO admin;
````

> You don’t need to create tables manually.
> On startup, **Flyway** will run the migrations from `src/main/resources/db/migration`
> (`V1__init_schema.sql`, `V2__users_and_borrow.sql`).

---

## Run the App (Local)

```bash
# build & run
mvn spring-boot:run

# or build a fat-jar and run it
mvn -DskipTests package
java -jar target/*.jar
```

Swagger UI → [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Run in Docker (App in container, DB on host)

```bash
mvn -DskipTests package
docker build -t library-service .

docker run --rm -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/library_service_2025 \
  -e SPRING_DATASOURCE_USERNAME=admin \
  -e SPRING_DATASOURCE_PASSWORD=admin \
  library-service
```

Stop: **Ctrl+C** (or `docker stop <container>`)

> Windows PowerShell: use backticks `` ` `` for line breaks or put the command on one line.

---

## Quick Start (Swagger/Postman)

**1) Create an author**

```http
POST /api/v1/authors
Content-Type: application/json
```

```json
{ "name": "J. K. Rowling", "birthDate": "1965-07-31" }
```

**2) Create a user**

```http
POST /api/v1/users
Content-Type: application/json
```

```json
{ "name": "Alice", "email": "alice@example.com" }
```

**3) Create a book**

```http
POST /api/v1/books
Content-Type: application/json
```

```json
{ "title": "HP1", "isbn": "1234567890", "authorId": "<AUTHOR_UUID>" }
```

**4) Borrow / Return a book**

```
POST /api/v1/books/{BOOK_ID}/borrow?userId={USER_ID}
POST /api/v1/books/{BOOK_ID}/return?userId={USER_ID}
```

---

## API Outline

* `/api/v1/books` — list/get/create (+ `/available`, `/{id}/borrow`, `/{id}/return`)
* `/api/v1/authors` — list/get/create (search by name supported)
* `/api/v1/users` — list/get/create (search by email supported)

---

## Project Structure

```
src/main/java/org/example/library
  controller/        # REST controllers
  service/impl/      # business logic
  repository/        # Spring Data JPA repositories
  model/             # JPA entities
  dto/request|response
  mapper/            # DTO mappers
  exception/         # custom exceptions & handlers
src/main/resources
  application.yml
  application-docker.yml
  db/migration/      # Flyway SQL migrations
Dockerfile
```

---

## Notes

* Default DB config can be changed in `application.yml` / env variables.
* Future improvements: pagination/sorting, unit/integration tests, docker-compose, CI.

```

