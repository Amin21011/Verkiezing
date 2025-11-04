# Elections

Welcome to the Elections project!  
This project combines a Spring Boot backend and a PostgreSQL database using **Docker Compose** to create a stable and identical development environment for the entire team.  
By using containers, everyone can start the backend and database with a single command — no manual database setup required.

### 🧱 Prerequisites
Before running the project, make sure you have:
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running
- [Java 21+](https://adoptium.net/) (for running Spring Boot)
- [Maven](https://maven.apache.org/) (optional for manual builds)
- (Optional) [DBeaver](https://dbeaver.io/) or [pgAdmin](https://www.pgadmin.org/) if you prefer a GUI to explore the database

---

## Running the Project

The project is designed to run via **Docker Compose**, which automatically starts both:
- A PostgreSQL database (`electiondb`)
- The Spring Boot backend (`election-backend`)

From the project root directory, simply run:

docker compose up -d
-d runs the containers in the background.

Once started, the backend is available at: http://localhost:8080

You can view the logs at any time:

    docker compose logs -f

To stop all containers:

    docker compose down

To completely reset the database (including stored data):

    docker compose down -v


### Application Configuration

The backend automatically connects to the PostgreSQL database defined in the Docker setup.
Connection details are managed in application.yml.

When running via Docker Compose:

spring.datasource.url=jdbc:postgresql://db:5432/electiondb
spring.datasource.username=postgres
spring.datasource.password=postgres

_When running locally (from IntelliJ or VS Code):_

spring.datasource.url=jdbc:postgresql://localhost:5432/electiondb
spring.datasource.username=postgres
spring.datasource.password=postgres

_Why did we make this choice?_

- Inside Docker, the backend connects to the service name db (the internal hostname).
- Outside Docker, your computer connects to localhost (the external mapped port).

### Database Information

All team members can use the same database credentials:

_Setting value:_

- Host (inside Docker): db
- Host (from local machine): localhost
- Port: 5432
- Database name: electiondb
- Username: postgres
- Password: postgres

This ensures consistency across all environments and prevents connection errors.