# NexusHR – HR Management System Backend

NexusHR is a backend application for a modern Human Resource Management System built using Spring Boot, Spring Security, JWT Authentication and PostgreSQL.

## 🚀 Features

* JWT-based Authentication
* Secure Login using Spring Security
* User Management
* Employee Management
* Employee CRUD Operations
* Dashboard Summary
* Payroll Management
* Payslip PDF Generation
* Leave Management
* Apply Leave
* Approve / Reject Leave
* Delete Leave
* PostgreSQL Database Integration
* RESTful APIs

## 🛠️ Technologies Used

* Java
* Spring Boot
* Spring Security
* JWT
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* OpenPDF

## 📦 Main Modules

### 🔐 Authentication

* User Login
* JWT Token Generation
* Password Encryption using BCrypt
* Protected API Endpoints

### 👥 Employee Management

REST APIs for:

* Create Employee
* Get All Employees
* Get Employee by ID
* Update Employee
* Delete Employee

### 💰 Payroll Management

Payroll module provides:

* Employee Salary Management
* Basic Salary
* Allowances
* Deductions
* Net Salary
* Payment Status
* Payslip PDF Download

### 🏖️ Leave Management

Leave module provides:

* Apply Leave
* View All Leaves
* View Employee Leaves
* View Leaves by Status
* Approve Leave
* Reject Leave
* Delete Leave

Leave statuses:

* PENDING
* APPROVED
* REJECTED

## 🗄️ Database

NexusHR uses PostgreSQL as the database.

Database name:

```text
nexushr
```

Default backend server:

```text
http://localhost:8080
```

## 🔗 API Endpoints

### Authentication

```text
POST /api/users/login
```

### Employees

```text
GET    /api/employees
GET    /api/employees/{id}
POST   /api/employees
PUT    /api/employees/{id}
DELETE /api/employees/{id}
```

### Payroll

```text
GET /api/payroll
GET /api/payroll/{id}/download
```

### Leave Management

```text
POST   /api/leaves
GET    /api/leaves
GET    /api/leaves/{id}
GET    /api/leaves/employee/{employeeId}
GET    /api/leaves/status/{status}
PUT    /api/leaves/{id}/approve
PUT    /api/leaves/{id}/reject
DELETE /api/leaves/{id}
```

### Dashboard

```text
GET /api/dashboard/summary
```

## ▶️ How to Run

### 1. Clone the repository

```bash
git clone https://github.com/rahulst099/nexushr-backend.git
```

### 2. Open the project

Open the project in IntelliJ IDEA or another Java IDE.

### 3. Configure PostgreSQL

Create a PostgreSQL database:

```text
nexushr
```

Configure the database connection in:

```text
src/main/resources/application.properties
```

### 4. Build the project

```bash
mvn clean install
```

### 5. Run the application

```bash
mvn spring-boot:run
```

The backend will start on:

```text
http://localhost:8080
```

## 🔐 Security

NexusHR uses:

* Spring Security
* JWT Authentication
* BCrypt Password Encryption
* Bearer Token Authorization

Protected APIs require:

```text
Authorization: Bearer <JWT_TOKEN>
```

## 📁 Project Structure

```text
nexushr-backend
│
├── src
│   └── main
│       ├── java
│       │   └── nexushr_backend
│       │       ├── user
│       │       ├── employee
│       │       ├── payroll
│       │       ├── leave
│       │       ├── dashboard
│       │       └── security
│       │
│       └── resources
│           └── application.properties
│
├── pom.xml
└── README.md
```

## 🔗 Frontend

NexusHR has a separate React frontend application.

**Frontend Repository:**

`nexushr-frontend`

## 👨‍💻 Project

**NexusHR – HR Management System**

Developed using Spring Boot, React.js and PostgreSQL.
