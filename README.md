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
- ✅ User details API: Fetch complete user profile, activities, and workout plans. 🔒 Admin-only access.
  
----

✅ User details API is available, which can fetch complete user profile along with activities and workout plans.
🔒 Access to all user details and activities is restricted to Admin only.
