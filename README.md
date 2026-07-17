# IPR Management System — Backend

A RESTful backend for the **Immovable Property Return (IPR) Management System**, built for government departments to manage employee property declarations through a simplified, department-level review and approval workflow.

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
The application supports a simplified user architecture with two main roles, holding login credentials together in the `users` table:

- **ROLE_HOD** — Head of Department. HOD is an employee under an office but has special HOD privileges. HOD reviews and approves/returns IPR submissions from regular employees in their department. Can also trigger deadline filing window notifications for offices.
- **ROLE_EMPLOYEE** — Regular Employee. Files IPR returns, declares properties, submits returns for HOD review, and tracks workflow status history.

### Core Modules

**IPR Return Workflow**
- Employees create and submit IPR returns.
- HOD reviews submissions from their department employees, approving them (marking as `APPROVED`) or returning them (marking as `RETURNED` with remarks).
- HOD can also file their own IPR returns (since they are also employees). To avoid HODs self-approving, their own returns are automatically marked as `FORWARDED`.
- Detailed audit logs track workflow actions (CREATED, SUBMITTED, APPROVED, RETURNED, etc.) for each IPR return.

**Property Declaration**
- Employees declare immovable properties (residential, commercial, land, etc.) linked to their active IPR returns.
- Supports adding, editing, and deleting property details before submitting the IPR.

**Declaration & Notifications**
- A formal declaration is attached to every submitted IPR return.
- HODs can create active deadline notifications to open/extend filing windows for offices under their department.

---

## Project Structure

```
src/main/java/com/nic/ipr/
├── auth/           # Login endpoint, auth service, token generation
├── config/         # Spring Security config, CORS, password encoding, app beans
├── controller/     # HodController, EmployeeController
├── dto/            # Request and response DTO definitions (Request/Response)
├── entity/         # JPA entities (User, Employee, Office, Department, IprReturn, Property, ...)
├── repository/     # Spring Data JPA repositories
├── service/        # Business logic interfaces and service implementations
└── shared/
    └── enums/      # Role (ROLE_HOD, ROLE_EMPLOYEE), WorkflowAction, IprStatus
```

---

## API Overview

All endpoints are prefixed with `/api/v1`.

| Prefix | Role Required | Description |
|--------|--------------|-------------|
| `/auth/login` | Public | Authenticate user credentials and receive JWT |
| `/hod/**` | ROLE_HOD | Department IPR review/approval, office filing window notifications, workflow logs |
| `/employee/**` | ROLE_EMPLOYEE / ROLE_HOD | Profile retrieval, IPR returns, properties, declarations |

---

## Running Locally

### Prerequisites
- Java 21 ([download](https://adoptium.net))
- PostgreSQL running locally ([download](https://www.postgresql.org/download))
- Git

No Maven installation is needed — the project includes the **Maven Wrapper** (`mvnw.cmd` / `mvnw`).

### Step 1 — Clone and configure

```bash
git clone https://github.com/knkkskr13/ipr-backend.git
cd ipr-backend
```

Open `src/main/resources/application-dev.properties` and set your local database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ipr
spring.datasource.username=postgres
spring.datasource.password=your_db_password
```

Create the database in PostgreSQL first if it doesn't exist:
```sql
CREATE DATABASE ipr;
```

### Step 2 — Seed Database manually
Since administrative endpoints for creating departments, offices, employees, and user credentials have been removed to keep the core workflow lightweight, you must seed them directly in the database before starting the API tests. 

See the SQL insert commands defined at the top of the **`src/test/resources/api-test.http`** file for standard test user seeding.

### Step 3 — Run the project

Pick any one method:

**Option A — Maven Wrapper (no installation needed)**

On Windows:
```cmd
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

On Mac/Linux:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

**Option B — IntelliJ IDEA**

Open the project folder in IntelliJ IDEA → it auto-detects the Maven project → click the green **Run** button. Ensure the active Spring profile is set to `dev` in the run configuration.

The API will be available at `http://localhost:8081`.

---

## Environment Variables (Production)

| Variable | Description |
|----------|-------------|
| `SPRING_PROFILES_ACTIVE` | Set to `prod` |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://host/dbname` |
| `SPRING_DATASOURCE_USERNAME` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | Database password |
| `JWT_SECRET` | Secret key (min 32 characters) |
| `JWT_EXPIRATION_MS` | Token expiry in ms (e.g. `86400000` = 24 hours) |

---

## Author

**Anik Sarkar**  
[GitHub](https://github.com/anikskr13)
