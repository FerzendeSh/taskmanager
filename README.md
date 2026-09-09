# TaskManager

A RESTful task management backend built with Java and Spring Boot.

## Tech Stack

- Java 17
- Spring Boot
- Gradle
- Spring Data JPA / Hibernate
- PostgreSQL
- JUnit 5
- Mockito
- Docker
- Docker Compose
- GitHub Actions
- GitHub Container Registry

## Features

- Create tasks
- Retrieve tasks
- Delete tasks
- Start and complete tasks
- Task state transitions
- Assign tasks to users
- Input validation
- Structured error handling
- PostgreSQL persistence

## Architecture

The application follows a layered architecture:

Client
→ Controller
→ Service
→ Repository
→ PostgreSQL

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/tasks` | Create a task |
| GET | `/tasks` | Get all tasks |
| GET | `/tasks/{id}` | Get a task |
| PATCH | `/tasks/{id}/start` | Start a task |
| PATCH | `/tasks/{id}/complete` | Complete a task |
| DELETE | `/tasks/{id}` | Delete a task |

## Run Locally

Set the database password:

```bash
export DB_PASSWORD=your_password