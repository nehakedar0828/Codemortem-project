# CodeMortem

CodeMortem is a backend-driven incident management system designed to help development teams track, analyze, and resolve software incidents efficiently. The platform enables structured incident logging, root cause analysis, and categorization through tags to identify recurring issues and improve system reliability.

## Features

- Create and manage incidents
- Root Cause Analysis (RCA) tracking
- Incident categorization using tags
- Search and filter incidents
- Relational data mapping using JPA
- RESTful APIs
- Exception handling and validation
- Database persistence with PostgreSQL

## Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok

## Architecture

```
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL Database
```

## Project Structure

```
src
├── controller
├── service
├── repository
├── entity
├── dto
├── exception
└── config
```

## API Endpoints

| Method | Endpoint | Description |
|----------|------------|------------|
| POST | /api/incidents | Create Incident |
| GET | /api/incidents | Get All Incidents |
| GET | /api/incidents/{id} | Get Incident By ID |
| PUT | /api/incidents/{id} | Update Incident |
| DELETE | /api/incidents/{id} | Delete Incident |

## Database Schema

The application stores:

- Incidents
- Root Causes
- Tags
- Incident-Tag Relationships

using PostgreSQL relational tables.

## Running the Application

### Clone Repository

```bash
git clone <repository-url>
cd codemortem-project
```

### Configure Database

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/codemortem_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Start Application

```bash
mvn clean install
mvn spring-boot:run
```

## Future Enhancements

- Authentication & Authorization (JWT)
- Incident Analytics Dashboard
- Notification Service Integration
- Email Alerts
- Incident Severity Levels
- Audit Logging

## Author

Neha Kedar
