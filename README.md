# IPR Management System — Backend

A RESTful backend for the **Immovable Property Return (IPR) Management System**, built for government departments to manage employee property declarations through a structured approval workflow.

**Live API:** `https://ipr-backend-lk3l.onrender.com`  
**Frontend:** [IPR-Frontend](https://github.com/anikskr13/IPR-Frontend)

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 4.0 |
| Security | Spring Security + JWT (JJWT 0.12.6) |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Validation | Jakarta Bean Validation |
| Build Tool | Maven |
| Containerization | Docker |

---

## Features

### Role-Based Access Control
Three roles with strictly separated permissions:

- **ROLE_AUTHORITY** — Top-level admin. Manages departments, offices, employees, and user credentials. Reviews and approves/returns IPR submissions from HODs.
- **ROLE_HOD** — Head of Department. Views and manages IPR returns from their department employees. Sends notifications and approves/returns submissions to Authority.
- **ROLE_EMPLOYEE** — Files IPR returns, declares properties, submits for HOD review, and tracks submission history.

### Core Modules

**Department & Office Management**
- Full CRUD for departments and offices
- Office to department hierarchy

**Employee Management**
- Employee registration and profile management
- Department-based employee grouping

**IPR Return Workflow**
- Employees create and submit IPR returns
- HOD reviews → approves or returns with remarks
- Authority reviews HOD-approved returns → final approval or return
- Full workflow audit log tracked at every stage

**Property Declaration**
- Employees declare immovable properties linked to their IPR return
- Full CRUD on property records before submission

**Declaration & Notifications**
- Formal declaration attached to each IPR return
- HOD sends notifications to offices regarding IPR deadlines

---

## Project Structure

```
src/main/java/com/nic/ipr/
├── auth/           # JWT filter, auth service, login endpoint
├── config/         # Security config, CORS, app beans
├── controller/     # AuthorityController, HodController, EmployeeController
├── dto/            # Request and response DTOs
├── entity/         # JPA entities (User, Employee, IprReturn, Property, ...)
├── repository/     # Spring Data JPA repositories
├── service/        # Business logic interfaces and implementations
└── shared/
    └── enums/      # Role enum (ROLE_AUTHORITY, ROLE_HOD, ROLE_EMPLOYEE)
```

---

## API Overview

All endpoints are prefixed with `/api/v1`.

| Prefix | Role Required | Description |
|--------|--------------|-------------|
| `/auth/login` | Public | Authenticate and receive JWT |
| `/authority/**` | ROLE_AUTHORITY | Department, office, employee, user, IPR management |
| `/hod/**` | ROLE_HOD | Department IPR returns, notifications, workflow |
| `/employee/**` | ROLE_EMPLOYEE / ROLE_HOD | Own IPR returns, properties, declarations |

---

## Running Locally

### Prerequisites
- Java 21 ([download](https://adoptium.net))
- PostgreSQL running locally ([download](https://www.postgresql.org/download))
- Git

No Maven installation needed — the project includes the **Maven Wrapper** (`mvnw`).

### Step 1 — Clone and configure

```bash
git clone https://github.com/anikskr13/IPR-Backend.git
cd IPR-Backend
```

Open `src/main/resources/application-dev.properties` and set your local DB credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ipr
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
```

Create the database in PostgreSQL first if it doesn't exist:
```sql
CREATE DATABASE ipr;
```

### Step 2 — Run the project

Pick any one method:

**Option A — Maven Wrapper (no installation needed)**

On Mac/Linux:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

On Windows:
```cmd
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

**Option B — IntelliJ IDEA**

Open the project folder in IntelliJ IDEA → it auto-detects the Maven project → click the green **Run** button. Make sure the active Spring profile is set to `dev` in the run configuration.

**Option C — Docker (no Java or Maven needed, just Docker)**

```bash
docker build -t ipr-backend .

docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/ipr \
  -e SPRING_DATASOURCE_USERNAME=your_db_username \
  -e SPRING_DATASOURCE_PASSWORD=your_db_password \
  -e JWT_SECRET=your-32-character-minimum-secret-key \
  -e JWT_EXPIRATION_MS=3600000 \
  ipr-backend
```

> On Windows/Mac use `host.docker.internal` to connect Docker to your local PostgreSQL. On Linux use `172.17.0.1` instead.

The API will be available at `http://localhost:8080`.

---

## Environment Variables (Production)

| Variable | Description |
|----------|-------------|
| `SPRING_PROFILES_ACTIVE` | Set to `prod` |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://host/dbname` |
| `SPRING_DATASOURCE_USERNAME` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | Database password |
| `JWT_SECRET` | Secret key (min 32 characters) |
| `JWT_EXPIRATION_MS` | Token expiry in ms (e.g. `3600000` = 1 hour) |

---

## Deployment

This project is deployed on **Render** using Docker.  
PostgreSQL is hosted on Render's managed database service.  
Auto-deploys on every push to the `main` branch.

---

## Author

**Anik Sarkar**  
[GitHub](https://github.com/anikskr13)
