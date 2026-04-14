# Booking App Backend

A Spring Boot backend for a simple room booking system. This project provides REST APIs for user registration and login, room listing, booking creation and management, booking availability checking, and basic admin user viewing.

## Overview

This backend is built for a course project and follows a common layered structure:

- **Controller** for REST API endpoints
- **Service** for business logic
- **Repository** for database access
- **Entity** for JPA models
- **DTO** for admin user responses
- **Config** for CORS setup

The system uses:

- **Java 17**
- **Spring Boot 3**
- **Spring Web**
- **Spring Data JPA**
- **MySQL**
- **Maven**

## Features

- User registration
- User login with session-based authentication
- Current user session check
- User sign out
- View all rooms
- Create a booking
- View current user's bookings
- View all bookings (admin only)
- Update booking dates (admin only)
- Cancel booking
- Check room availability by date range
- View all users (admin only)
- Simple API/database test endpoints

## Project Structure

```text
src/
└── main/
    ├── java/comp4443/booking_app/
    │   ├── config/
    │   │   └── CorsConfig.java
    │   ├── controller/
    │   │   ├── AdminUserController.java
    │   │   ├── BookingController.java
    │   │   ├── DBTestController.java
    │   │   ├── RoomController.java
    │   │   ├── TestController.java
    │   │   └── UserController.java
    │   ├── dto/
    │   │   └── AdminUserDto.java
    │   ├── entity/
    │   │   ├── Booking.java
    │   │   ├── Room.java
    │   │   └── User.java
    │   ├── repository/
    │   │   ├── BookingRepository.java
    │   │   ├── RoomRepository.java
    │   │   └── UserRepository.java
    │   ├── service/
    │   │   ├── AdminUserService.java
    │   │   ├── BookingService.java
    │   │   └── RoomService.java
    │   └── BookingAppApplication.java
    └── resources/
        └── application.properties
```

## Database Design

### `users`
Stores registered users.

Suggested fields based on the entity:

- `id`
- `username`
- `password`
- `created_at`
- `role` (`0 = admin`, `1 = normal user`)

### `rooms`
Stores room information.

Suggested fields:

- `id`
- `name`
- `price`

### `bookings`
Stores booking records.

Suggested fields:

- `id`
- `user_id`
- `room_id`
- `check_in`
- `check_out`

## API Endpoints

### User

#### Register
`POST /register`

Request body:

```json
{
  "email": "user@example.com",
  "password": "123456",
  "role": 1
}
```

#### Login
`POST /login`

Request body:

```json
{
  "email": "user@example.com",
  "password": "123456"
}
```

#### Get current user
`GET /me`

#### Sign out
`POST /signout`

---

### Rooms

#### Get all rooms
`GET /rooms`

---

### Bookings

#### Create booking
`POST /bookings`

Request body:

```json
{
  "roomId": 1,
  "checkIn": "2026-04-20",
  "checkOut": "2026-04-22"
}
```

> `userId` is taken from the current login session on the backend.

#### Get my bookings
`GET /bookings/my`

#### Get all bookings
`GET /bookings/all`

> Admin only.

#### Update booking dates
`PUT /bookings/{id}`

Request body:

```json
{
  "checkIn": "2026-04-23",
  "checkOut": "2026-04-25"
}
```

> Admin only.

#### Delete booking
`DELETE /bookings/{id}`

- Admin can delete any booking
- Normal user can delete only their own booking

#### Check room availability
`GET /bookings/check?roomId=1&checkIn=2026-04-20&checkOut=2026-04-22`

---

### Admin

#### Get all users
`GET /admin/users`

---

### Test

#### Test API
`POST /test`

#### Test database connection
`GET /test/db`

## Business Rules

The booking service includes several checks:

- User ID must exist
- Room ID must exist
- Check-in and check-out dates are required
- Check-out date must be after check-in date
- Duplicate booking for the same user, room, and date range is blocked
- Overlapping bookings for the same room are blocked

## Local Setup

### 1. Clone the repository

```bash
git clone https://github.com/herb4794/Comp4443_backend_spring_boot_java.git
cd Comp4443_backend_spring_boot_java
```

### 2. Prepare MySQL

Create a MySQL database:

```sql
CREATE DATABASE comp4442;
```

### 3. Configure application properties

Update `src/main/resources/application.properties`:

```properties
spring.application.name=booking-app
spring.datasource.url=jdbc:mysql://localhost:3306/comp4442
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> If your local MySQL username or password is different, change them before starting the app.

### 4. Run the project

Using Maven Wrapper:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Or build the JAR:

```bash
./mvnw clean package
java -jar target/booking-app-0.0.1-SNAPSHOT.jar
```

## Frontend Connection

The project currently allows CORS requests from:

```text
http://localhost:5173
```

This means it is ready to connect to a frontend running on Vite's default development port.

If you deploy the frontend, remember to update the allowed origin in `CorsConfig.java` and controller-level `@CrossOrigin` settings.

## Notes

- Authentication is currently session-based.
- Passwords are stored directly in the database in the current implementation.
- This project is suitable as a learning or course-demo backend.
- For production use, you should add password hashing, validation, exception handling, and stronger access control.

## Future Improvements

- Encrypt passwords with BCrypt
- Add DTO validation with `@Valid`
- Add global exception handling
- Add Swagger / OpenAPI documentation
- Add pagination and filtering
- Add unit and integration tests
- Use environment variables for database credentials
- Support deployment to Render, Railway, or AWS

## Author

Developed for a PolyU course project.

---

If this project is used together with a React frontend, the backend acts as the REST API and handles user login state, room data, and booking records.
