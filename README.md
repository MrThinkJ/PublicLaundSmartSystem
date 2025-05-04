# Public Laundry Smart System

A smart management system for public laundry services, built with Spring Boot and PostgreSQL for integration with IOT devices through Firebase.

## Features

- User account management
- Laundry machine monitoring and control
- Payment integration with PayOS
- Real-time notifications using Firebase
- Websocket communication for live updates
- Secure API with JWT authentication

## Prerequisites

- Docker
- Docker Compose
- Java 21 (for local development)
- Maven (for local development)

## Project Structure

The project is containerized using Docker with two main services:

- PostgreSQL database
- Spring Boot application (Java 21, Spring Boot 3.3.4)

## Quick Start

1. Clone the repository:

```
git clone https://github.com/MrThinkJ/PublicLaundSmartSystem.git
```

2. Navigate to the project directory:

```
cd PublicLaundSmartSystem
```

3. Make sure you have the following files in your project root:

- `docker-compose.yml`
- `Dockerfile`
- Firebase configuration file at `src/main/resources/config/firebase_key.json`

4. Start the application using Docker Compose:

```
docker compose up -d
```

This will:

- Start a PostgreSQL database container
- Build and start the Spring Boot application container

## Configuration

### Database Configuration

The PostgreSQL database is configured with the following settings:

- Port: 5432
- Database name: laundry_system_db
- Username: postgres
- Password: 28122003
- Timezone: Asia/Ho_Chi_Minh

### Application Configuration

The Spring Boot application is configured with:

- Port: 8080
- Java version: 21
- Spring Boot version: 3.3.4

Application properties are located in:

- `src/main/resources/application.properties`

You need to set these security keys in your application.properties:

For JWT secret:

```
openssl rand -base64 32
```

For machine secret:

```
openssl rand -base64 32
```

## Technologies Used

- **Backend**: Spring Boot, Spring Security, Spring Data JPA
- **Database**: PostgreSQL
- **Authentication**: JWT
- **Real-time Communication**: WebSockets, Firebase
- **Payment Integration**: PayOS
- **Containerization**: Docker, Docker Compose

## Notes

- The application uses Firebase for notifications and real-time database functionality
- Make sure to properly configure the Firebase credentials in `firebase_key.json`

## License

This project is licensed under the Apache License 2.0
