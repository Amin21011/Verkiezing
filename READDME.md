# Elections

Welcome to the Elections project!

This application is built with Spring Boot (Java 21) and uses PostgreSQL as its production database, managed through Docker Compose.

The setup ensures every team member can run the backend and database identically — no manual setup needed.

---

## Overview

Environment | Database | Profile | Description  
| -- | -- | -- | -- |  
Development | H2 (in-memory) | dev | Used for local testing without Docker  
Production / Docker | PostgreSQL 16 | prod | Used for team-wide, stable environment  

---

## General Information

### _Why PostgreSQL?_

- Reliable, scalable, and ideal for long-term persistence.
- The Docker container ensures every team member uses the same consistent setup.

### _Why This Setup?_

- **Consistency:** All developers run the same environment using a single command.
- **Profiles:** Clear separation between dev (H2) and prod (PostgreSQL).
- **Port Mapping:** PostgreSQL runs on port **5433** externally to avoid conflicts with local installations.
- **Persistence:** Docker volume `postgres_data` ensures data is preserved across restarts.

---

## Prerequisites

Before running the project, make sure you have:

- Docker Desktop — for running the database and backend
- Java 21+ — to build and run the Spring Boot app
- Maven — optional, for manual builds
- **(Optional) Beekeeper Studio** — recommended GUI for inspecting the PostgreSQL database

---

## Running the Project (via Docker)

From the project root directory, run:
    
    docker compose up -d

`-d` runs the containers in the background.

The backend automatically connects to the PostgreSQL service inside Docker.

Once running:

- **Backend:** http://localhost:8080
- **Database:** `localhost:5432`
- **Frontend (local):** http://localhost:5173

To view container logs:

    docker compose logs -f

To stop all containers:

    docker compose down

To completely reset the database (including stored data):

    docker compose down -v

## Team Database Access (Neon)

This project uses a shared Neon PostgreSQL database for production.

Team members do not need a Neon account — only a personal database login. Each developer has their own credentials for security and auditing.

### Connection Settings

Use these settings in your application-dev.yml:
    
    spring:
        datasource:
        url: jdbc:postgresql://ep-nameless-resonance-abdz6eaj-pooler.eu-west-2.aws.neon.tech/neondb?sslmode=require&channelBinding=require
        username: <your_username>
        password: <your_password>
        driver-class-name: org.postgresql.Driver
    
    jpa:
        hibernate:
        ddl-auto: update
        show-sql: true

### Connecting Through DB Tools (Optional)

You can use these settings in:

- IntelliJ Database Tool
- DBeaver
- Beekeeper Studio

    Host: ep-nameless-resonance-abdz6eaj-pooler.eu-west-2.aws.neon.tech
    Default Database: neondb
    Port: 5432
    SSL: required
    Username: <your_username>
    Password: <your_password>

### Important Rules

- Do not modify or drop tables
- Contact admin (Morsal) for permission issues
- SSL error → ensure the URL includes ?sslmode=require&channelBinding=require
- “Permission denied” → request updated privileges
- No data visible → ensure active Spring profile is dev

Once credentials are added, you can run the backend and start querying the shared Neon database.


