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

## Integrate with Ollama
### download Ollama from https://ollama.com/download/windows
### follow instruction at: https://docs.spring.io/spring-ai/reference/api/chat/ollama-chat.html
### run the following command to start the Ollama server: https://gist.github.com/karthiks/fb89ea8230e6574200c8644d9ac878b4
```bash
ollama pull mistral
ollama pull nomic-embed-text   # optional for embeddings
ollama serve
ollama run mistral --port 11434 num_ctx 2048
ollama stop mistral
```

## Install Ollama UI locally
```
pip install open-webui
open-webui serve
```




