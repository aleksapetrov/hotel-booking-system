# Hotel Booking System

A simple hotel booking reservation system inspired by a program I worked with during my previous experience. It allows users to create, view, and manage bookings, with role-based access control for clients, companies, hotels, agents, and admins.

## Features
- Create, view, update, and delete bookings
- Role-based access control:
  - **Client:** Manage own bookings
  - **Company:** Manage company bookings and clients
  - **Hotel:** Manage hotel info and bookings
  - **Agent/Admin:** Full access to bookings and users
- Secure authentication with OAuth2 and JWT tokens

## Tech Stack
- **Java 17**
- **Spring Boot (Security, Data JPA, OAuth2)**
- **Thymeleaf**
- **H2 Database**

## How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/<your-username>/hotel-booking-system.git
