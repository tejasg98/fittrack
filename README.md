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

