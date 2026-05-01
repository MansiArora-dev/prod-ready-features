# 🚀 Production Ready Features — Spring Boot

A Spring Boot application demonstrating production-ready features including JPA auditing, Hibernate Envers, RestClient for third-party API integration, structured logging with Logback, Spring Boot Actuator for monitoring, and OpenAPI/Swagger for API documentation.

---

## ☁️ Core Concepts Overview

| Feature | Description |
|---------|-------------|
| JPA Auditing | Auto-managed `createdBy`, `createdDate`, `updatedBy`, `updatedDate` fields |
| Hibernate Envers | Full revision history and audit trail for entities |
| RestClient | Type-safe HTTP client for third-party API integration |
| Logging | Structured logging with SLF4J, Logback and log levels |
| Spring Boot Actuator | Health checks, metrics and monitoring endpoints |
| OpenAPI/Swagger | Auto-generated API documentation |
| DevTools | Hot reload for faster development |

---

## 🛠️ What I Built

### 1. JPA Auditing + Hibernate Envers
- `AuditableEntity` — base class with `@CreatedDate`, `@LastModifiedDate`, `@CreatedBy`, `@LastModifiedBy`
- `AuditorAwareImpl` — provides current user for auditing
- `PostEntity` — extends `AuditableEntity` with `@Audited` for revision history
- `AuditController` — `/audit/posts/{postId}` endpoint to fetch full revision history

### 2. RestClient Integration
- `EmployeeClient` — interface defining Employee API operations
- `EmployeeClientImpl` — RestClient implementation with error handling and logging
- `RestClientConfig` — configures RestClient with base URL and default headers
- Supports GET, POST operations with `4xx` and `5xx` error handling

### 3. Logging
- SLF4J with Logback — structured logging across all layers
- Log levels — `TRACE`, `DEBUG`, `INFO`, `ERROR` used appropriately
- `logback-spring.xml` — rolling file appender with size and time based policy
- Package-level log configuration in `application.properties`

### 4. Spring Boot Actuator
- All endpoints exposed — `management.endpoints.web.exposure.include=*`
- Info endpoints — app version, build info, git info, Java and OS details
- Health checks available at `/actuator/health`

### 5. OpenAPI/Swagger
- Auto-generated API documentation
- Available at `http://localhost:9000/swagger-ui.html`
- API specs at `http://localhost:9000/v3/api-docs`

---

## 🔧 Core Concepts Demonstrated

### JPA Auditing
```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Audited
public class AuditableEntity {
    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;
}
```

### Hibernate Envers — Revision History
```java
@GetMapping(path = "/audit/posts/{postId}")
List<PostEntity> getPostRevisions(@PathVariable Long postId) {
    AuditReader reader = AuditReaderFactory.get(entityManagerFactory.createEntityManager());
    List<Number> revisions = reader.getRevisions(PostEntity.class, postId);
    return revisions.stream()
            .map(rev -> reader.find(PostEntity.class, postId, rev))
            .collect(Collectors.toList());
}
```

### RestClient with Error Handling
```java
ApiResponse<List<EmployeeDTO>> response = restClient.get()
        .uri("employees")
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
            throw new ResourceNotFoundException("Employee not found");
        })
        .body(new ParameterizedTypeReference<>() {});
```

### Logging Levels
```java
log.trace("Trying to retrieve all employees");
log.info("Attempting to call restClient");
log.debug("Successfully retrieved employees");
log.error("Exception occurred", e);
```

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/posts` | Get all posts |
| `GET` | `/posts/{postId}` | Get post by ID |
| `POST` | `/posts` | Create new post |
| `PUT` | `/posts/{postId}` | Update post |
| `GET` | `/audit/posts/{postId}` | Get post revision history |
| `GET` | `/actuator/health` | Application health check |
| `GET` | `/actuator/info` | Application info |
| `GET` | `/swagger-ui.html` | Swagger UI |

---

## 🚀 Quick Start

**Prerequisites:** Java 21+, Maven, MySQL

```bash
git clone https://github.com/MansiArora-dev/prod-ready-features.git
cd prod-ready-features
```

**Setup MySQL:**
1. Create database: `CREATE DATABASE <your_database_name>;`
2. Create `application-local.properties` in `src/main/resources/` with your credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/<your_database_name>?useSSL=false
spring.datasource.username=<your_username>
spring.datasource.password=<your_password>
```
> ⚠️ Add `application-local.properties` to `.gitignore` to avoid committing credentials

**Using IntelliJ IDEA (Recommended):**
1. Open project in IntelliJ
2. Run → **Edit Configurations**
3. Environment variables: `SPRING_PROFILES_ACTIVE=local`
4. Click **Run ▶️**

**Using Maven:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

---

## 📂 Project Structure

```
src/main/java/com/springboot/prodreadyfeatures/
├── advice/
│   ├── ApiError.java                  # Error response structure
│   ├── ApiResponse.java               # Generic response wrapper
│   └── GlobalExceptionHandler.java    # Centralized exception handling
├── auth/
│   └── AuditorAwareImpl.java          # Current user provider for auditing
├── clients/
│   ├── impl/
│   │   └── EmployeeClientImpl.java    # RestClient implementation
│   └── EmployeeClient.java            # Employee API client interface
├── config/
│   ├── AppConfig.java                 # JPA auditing + ModelMapper config
│   └── RestClientConfig.java          # RestClient bean configuration
├── controllers/
│   ├── AuditController.java           # Revision history endpoints
│   └── PostController.java            # Post CRUD endpoints
├── dto/
│   ├── EmployeeDTO.java               # Employee data transfer object
│   └── PostDTO.java                   # Post data transfer object
├── entities/
│   ├── AuditableEntity.java           # Base auditing entity
│   └── PostEntity.java                # Post entity with audit support
├── exceptions/
│   └── ResourceNotFoundException.java # Custom runtime exception
├── repositories/
│   └── PostRepository.java            # JPA Repository
└── services/
├── impl/
│   └── PostServiceImpl.java       # Post service implementation
└── PostService.java               # Post service interface
src/main/resources/
├── application.properties             # App configuration
└── logback-spring.xml                 # Logback configuration
```

---

## 💻 Technologies

- **Java 21** | **Spring Boot** | **Maven**
- **Spring Data JPA** | **MySQL**
- **Spring Boot Actuator** | **Hibernate Envers** | **SpringDoc OpenAPI**

---

## 🌟 Key Takeaways
- **JPA Auditing** — Auto-tracks who created/modified records and when
- **Hibernate Envers** — Complete revision history without manual tracking
- **RestClient** — Modern, fluent API for HTTP calls with proper error handling
- **Structured Logging** — Different log levels for different environments
- **Actuator** — Production monitoring without extra code
- **OpenAPI** — Auto-generated, always up-to-date API documentation

---

## 👩‍💻 Developer
**Mansi Arora** — Software Engineer