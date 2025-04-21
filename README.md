# Template API

A template for building Kotlin APIs with Spring Boot, SB Admin monitoring, structured logging, Prometheus metrics, and
versioned configuration.

## Features

- **Gradle Kotlin DSL**: Build configuration using Kotlin DSL.
- **Spring Boot**: Robust framework for building microservices.
- **Spring Boot Admin**: Monitoring and management of Spring Boot applications.
- **Elastic ECS Logging**: Structured logging compatible with Elastic Common Schema.
- **Redis**: Integrated caching mechanism.

### Routes

Note: all API routes have the context path `/template-api-kotlin`.

```
GET    | /actuator

GET    | /app-version
```

### Repository file tree

```
├── deploy/
│   ├── application.yml      # API properties file
│   ├── deployment.yaml      # Kubernetes manifest file
│   ├── log4j2-spring.xml    # Log configuration file
│
├── src/main/
│   ├── kotlin/com.it.template.api
│   │   ├── client               # WebClient classes
│   │   ├── configuration        # Configuration classes
│   │   │   ├── component        # Spring Security components
│   │   │   ├── handler          # Spring Security handlers
│   │   │   ├── interceptor      # Spring web interceptors
│   │   ├── evaluator            # Spring Security permission evaluator
│   │   ├── exception.handler    # Spring exception handler
│   │   ├── handler              # API routers handlers
│   │   │   ├── function         # Filters for routes handlers
│   │   ├── model.exception      # Model classes
│   │   ├── repository.cache     # Repository classes
│   │   ├── router               # API routers classes
│   │   ├── service.cache        # Service classes
│   │
│   ├── resources
│   │   ├── application.yml      # API properties file (local)
│
├── build.gradle.kts       # Gradle build script
├── log4j2-spring.xml      # Log configuration file (local)
├── settings.gradle.kts    # Gradle settings
│
├── .github/workflows/
│   ├── build.yml
│   ├── lint.yml
│   ├── sast.yml
│   ├── test.yml
```
