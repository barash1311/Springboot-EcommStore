 # Spring Boot E-Commerce Store
 
A complete backend system for an **E-Commerce Store**, built using **Spring Boot**, **Spring Security**, **Spring Data JPA**, and **MySQL**.  
This project provides secure authentication, product management, cart handling, order processing, and admin analytics.

---

## ✨ Overview

This backend powers a modern e-commerce platform with a layered architecture:
- **Controller Layer:** Handles REST API endpoints.
- **Service Layer:** Contains business logic.
- **Repository Layer:** Manages data persistence using JPA.
- **DTO Layer:** Maps entities to clean API responses.
- **Security Layer:** Implements JWT-based authentication and role-based access control.

---

##  Features

###  User & Authentication
- Register and log in users with **JWT tokens**.
- Role-based access control (**Admin** and **User**).
- Password encryption using **BCrypt**.

###  Product Management
- Full CRUD operations for products.
- Admin-only endpoints for product creation and updates.
- Public endpoints for browsing products.

###  Cart Management
- Add, update, or remove products from the cart.
- Automatically calculates total price.
- Each user has their own cart.

###  Order Management
- Create orders from cart items.
- Track order history per user.
- Update order status (Admin feature).

###  Address Management
- Add and manage multiple addresses per user.
- Used during checkout and order creation.

###  Analytics (Admin)
- View total users, orders, and revenue.
- Future-ready for dashboard integration.

###  Additional Features
- Global exception handling with custom error responses.
- DTO mapping using **ModelMapper**.
- Validation using **Jakarta Validation**.
- Swagger UI integration for API documentation.

---

---

##  Tech Stack

| Category | Technology |
|-----------|-------------|
| Language | **Java 17** |
| Framework | **Spring Boot 3** |
| Security | **Spring Security (JWT)** |
| ORM | **Hibernate / JPA** |
| Database | **MySQL** |
| Mapping | **ModelMapper** |
| Build Tool | **Maven** |
| Documentation | **Swagger (SpringDoc OpenAPI)** |
| Utilities | **Lombok**, **Jakarta Validation** |

---
