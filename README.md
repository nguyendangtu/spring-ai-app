# Stock Management Spring Boot App

A Spring Boot 3.4 project with Java 21 that supports CRUD for stocks, MySQL persistence, Swagger UI, and a simple frontend dashboard.

## Features
- Stock CRUD API
- MySQL database support
- Swagger / OpenAPI documentation
- Search, sort, and pagination
- Simple browser dashboard
- Docker Compose setup

## Run locally

```bash
mvn spring-boot:run
```

Then open:
- `http://localhost:8081/`
- `http://localhost:8081/swagger-ui/index.html`

## Run with Docker Compose

```bash
docker compose up --build
```

Services:
- MySQL: `localhost:3306`
- App: `http://localhost:8081`

## Example API payload

```json
{
  "symbol": "AAPL",
  "companyName": "Apple Inc",
  "price": 210.5,
  "quantity": 25
}
```

## Main endpoints

- `GET /api/stocks`
- `GET /api/stocks/{id}`
- `POST /api/stocks`
- `PUT /api/stocks/{id}`
- `DELETE /api/stocks/{id}`

## MySQL environment variables

```bash
DB_URL=jdbc:mysql://localhost:3306/stockdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=your_password
```
