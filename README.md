# 🏋️‍♂️ FitTrack - Fitness Tracking Application

A **Spring Boot-based fitness tracking application** that allows users to manage **workout plans** and **activity logs**, with **secure JWT authentication** and **role-based access control**.

---

## 🚀 Features

- ✅ **User Registration & Login** using **JWT Authentication**
- ✅ **Password Encryption** with `BCryptPasswordEncoder`
- ✅ **JWT stored in HttpOnly cookies** (secure, not accessible via JS)
- ✅ **Role-based access control** (USER, ADMIN)
- ✅ **Users Authentication and Authorization**
- ✅ **Users Management**(CRUD with pagination)
- ✅ **Workout Plans Management** (CRUD with pagination)
- ✅ **Activity Logs Management** (CRUD with pagination)
- ✅ **Validation** using Jakarta annotations
- ✅ **Swagger/OpenAPI documentation**
- ✅ **Unit & Integration Tests** using JUnit + Mockito + MockMvc
- ✅ **Exception Handling** with global `@ControllerAdvice`
- ✅ **Storing Data In H2-Databse** (In Memory)
- ✅ **Using Logger For Tracing**

---

## 🔐 Authentication & Security

- JWT is returned in **HttpOnly cookie** → prevents XSS.
- **Password hashing** with BCrypt (no plain text passwords).
- **No Refresh Token** (tokens expire after X mins, re-login required).
- Users cannot modify other users’ details (secured at service layer).
----

## 📊 Code Quality & Reports

- ✅ SonarQube: No major issues, code smells, or vulnerabilities.
- ✅ Test Coverage: >80% across modules.
- ✅ Unit & Integration Tests: All test cases pass (`mvn test`).

## 📖 API Documentation
- ✅ Swagger docs: [UI](http://localhost:8080/swagger-ui/index.html) | [API](http://localhost:8080/v3/api-docs)
  
----

✅ User details API is available, which can fetch complete user profile along with activities and workout plans.
🔒 Access to all user details and activities is restricted to Admin only.

----

## Prerequisites
- Java 24
- Maven 3.6+

## Installation & Setup
Clone the repository:
git clone https://github.com/tejasg98/fittrack.git
cd fittrack

Configure database:
For development (H2): No setup needed
For production: Update application.properties with your database credentials

Build the application:
mvn clean install

Run the application:
mvn spring-boot:run

Access the application:
Main application: http://localhost:8080
H2 Console (dev): http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:fittrack
Username: sa
Password: (leave empty) 
