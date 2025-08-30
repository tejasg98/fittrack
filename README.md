# 🏋️‍♂️ FitTrack - Fitness Tracking Application

A **Spring Boot-based fitness tracking application** that allows users to manage **workout plans** and **activity logs**, with **secure JWT authentication** and **role-based access control**.

---

## 🚀 Features

- ✅ **User Registration & Login** using **JWT Authentication**
- ✅ **Password Encryption** with `BCryptPasswordEncoder`
- ✅ **JWT stored in HttpOnly cookies** (secure, not accessible via JS)
- ✅ **Role-based access control** (USER, ADMIN)
- ✅ **Workout Plans Management** (CRUD)
- ✅ **Activity Logs Management** (CRUD with pagination)
- ✅ **Validation** using Jakarta annotations
- ✅ **Swagger/OpenAPI documentation**
- ✅ **Unit & Integration Tests** using JUnit + Mockito + MockMvc
- ✅ **Exception Handling** with global `@ControllerAdvice`

---

## 🔐 Authentication & Security

- JWT is returned in **HttpOnly cookie** → prevents XSS.
- **Password hashing** with BCrypt (no plain text passwords).
- **No Refresh Token** (tokens expire after X mins, re-login required).
- Users cannot modify other users’ details (secured at service layer).
----
## 📊 Code Quality & Reports

✅ SonarQube report: No major issues, code smells, or vulnerabilities detected.
✅ Test Coverage: Above 80% across modules.
✅ Unit & Integration Tests: All test cases pass successfully (mvn test).
----
## API Documentatio
✅ SonarQube report is clean (no major issues).
✅ Test coverage is above 80%.
✅ Swagger documentation is available at:
http://localhost:8080/swagger-ui/index.html
http://localhost:8080/v3/api-docs
----

✅ User details API is available, which can fetch complete user profile along with activities and workout plans.
🔒 Access to all user details and activities is restricted to Admin only.
