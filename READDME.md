# Elections

Welcome to the Elections project!

This application is built with Spring Boot (Java 21) and uses PostgreSQL as its production database, managed through Docker Compose.

The setup ensures every team member can run the backend and database identically — no manual setup needed.

### Overview

Environment	Database | Profile | Description
| -- | -- | -- |
Development	H2 (in-memory) | dev | Used for local testing without Docker
Production / Docker	PostgreSQL 16 | prod| Used for team-wide, stable environment


### General information

_Why PostgreSQL?_

- Reliable, scalable, and ideal for long-term persistence.
- The Docker container ensures everyone uses the same setup.

_Why This Setup?_

- Consistency: Every developer runs the same environment with one command.
- Profiles: Clear separation between dev (H2) and prod (PostgreSQL).
- Port Mapping: PostgreSQL runs on 5433 externally to avoid conflicts with other local instances.
- Persistence: Docker volume postgres_data keeps your data safe even after restarts


### Prerequisites

Before running the project, make sure you have:

- Docker Desktop: for running containers
- Java 21+: to build and run the Spring Boot app
- Maven (optional, for manual builds)
- (Optional) DBeaver (or pgAdmin): to explore the PostgreSQL database


### Running the Project (via Docker)

From the project root directory, simply run:

    docker compose up -d

-d runs the containers in the background.

The backend automatically connects to the PostgreSQL service inside Docker.

Once running:

- Backend: http://localhost:8080
- Database: localhost:5433
- Frontend (if running locally): http://localhost:5173

To view container logs:

    docker compose logs -f

To stop all containers:

    docker compose down


To completely reset the database (including stored data):

    docker compose down -v


### Database Information

All team members can use the same database credentials:

_Setting value:_

- Host (inside Docker): db
- Host (from local machine): localhost
- Port: 5433
- Database name: electiondb
- Username: postgres
- Password: postgres

This ensures consistency across all environments and prevents connection errors.