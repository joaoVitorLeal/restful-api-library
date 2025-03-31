# Library API RESTful

[![Java 21](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring_Security-6.x-red.svg)](https://spring.io/projects/spring-security)
[![Spring Boot Actuator](https://img.shields.io/badge/Spring_Boot_Actuator-enabled-green.svg)](https://spring.io/projects/spring-boot#actuator)
[![JWT](https://img.shields.io/badge/JWT-enabled-blueviolet.svg)](https://jwt.io)
[![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-3.x-orange.svg)](https://spring.io/projects/spring-data-jpa)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16.3-blue.svg)](https://www.postgresql.org)
[![Swagger/OpenAPI](https://img.shields.io/badge/Swagger_OpenAPI-2.6.0-orange.svg)](https://springdoc.org)
[![Docker](https://img.shields.io/badge/Docker-20.10-blue.svg)](https://www.docker.com)
[![Postman](https://img.shields.io/badge/Postman-Agent-blue.svg)](https://www.postman.com)
[![JUnit Jupiter](https://img.shields.io/badge/JUnit_Jupiter-5.x-green.svg)](https://junit.org/junit5)

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation & Setup](#installation--setup)
  - [Prerequisites](#prerequisites)
  - [Environment Configuration](#environment-configuration)
  - [Database Setup](#database-setup)
  - [Running Locally](#running-locally)
  - [Using Docker (Optional)](#using-docker-optional)
- [API Documentation](#api-documentation)
- [Testing](#test-and-consume-the-api-via-postman-agent)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)
- [Acknowledgements](#acknowledgements)

---

## Overview

Library API RESTful is a robust backend service built with Java 21 and Spring Boot 3.3.6 that manages a library system. It offers CRUD operations for books, authors, clients, and users while providing secure authentication using JWT and OAuth2. The application is containerized with Docker for production deployment, though you may connect directly to a locally installed PostgreSQL instance.

---

## Features

- **CRUD Operations:** 📚 Manage books, authors, clients, and users.
- **Authentication & Authorization:** 🔐 Secure endpoints using JWT and role-based access control via Spring Security.
- **OAuth2 Social Login:** 🔑 Integration with Google authentication.
- **Validation & Exception Handling:** ✅ Comprehensive input validation and custom error responses.
- **API Documentation:** 📜 Interactive documentation via Swagger/OpenAPI.
- **Transactional Support:** 💼 Advanced JPA transaction handling.
- **Containerization:** 🐳 Optional Docker deployment for the API, PostgreSQL, and pgAdmin4.

---

## Technologies Used

- **Java 21**
- **Spring Boot 3.3.6**
- **Spring Security 6.x**
- **Spring Boot Actuator:**
- **JWT (JSON Web Tokens)**
- **Spring Data JPA**
- **PostgreSQL 16.3**
- **Swagger/OpenAPI 2.6.0**
- **Docker**
- **Postman Agent:** Used for API testing and consumption
- **JUnit Jupiter 5.x**

---

## Installation & Setup

### Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **PostgreSQL 16.3** (or a locally installed PostgreSQL instance)
- **Docker** (optional, for containerized deployment)

---

### Environment Configuration

Create an `env.vars` file (or set environment variables) with your database credentials and other secrets. For example:

```yaml
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:<port>/<database>
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
SPRING_PROFILES_ACTIVE=production
TZ=America/Sao_Paulo
```
---

### Database Setup

The file `commands-sql.txt` contains the SQL commands necessary to create the database schema and sample data. These commands include:
- Table creation for author, book, users, and client.
Review commands-sql.txt for detailed SQL statements, then execute them in your PostgreSQL environment (either locally or via a container).

---

### Getting Started

1. **Clone the repository:**
```bash
    git clone https://github.com/joaoVitorLeal/restful-api-library.git
```
2. **Build the project with Maven:** (at root directory)
```bash
    ./mvnw clean install
```
3. **Run the application** (at root directory)
```bash
    java -jar ./target/libraryapi-0.0.1-SNAPSHOT.jar
```
####  Access the API at URL: http://localhost:8080.

## Important Information

⚠️ **Logging Enabled:**  
This application has logging enabled, generating log reports and storing them in a log file each time the application is run. Over time, this may cause the application to become slower or consume more memory on the machine. However, **logging for observability can be disabled** in the `application.yml` file, in the following section:

```yaml
# Log configuration
logging:
  pattern:
    console: '%clr(%d{dd-MM-yyyy HH:mm:ss}){faint} %clr(%5p) %clr([%t]){faint} %clr(%-40.40logger){cyan} %clr(:){yellow} %m%n'
  file:
    name: logs/app.log
  level:
    root: warn
    io:
      github:
        joaoVitorLeal: trace
    org:
      springframework:
        web: debug
        security: trace
```

### Using Docker (Optional)
  #### ***For Linux Systems:***
If you prefer containerized deployment, the file commands-docker.txt provides commands to:
 - ***Create a Docker network:***
```bash
 sudo docker network create library-network
 ```
- ***Run a PostgreSQL container*** (version 16.3):
```bash
 sudo docker run --name librarydb -p 5432:5432 -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -e POSTGRES_DB=library --network library-network -d postgres:16.3
 ```
- ***Run a pgAdmin4 container:***
```bash
 sudo docker run --name pgadmin4 -p 15432:80 -e PGADMIN_DEFAULT_EMAIL=joaoleal98@outlook.com -e PGADMIN_DEFAULT_PASSWORD=admin --network library-network -d dpage/pgadmin4
 ```
  #### ***For Windows Systems***
- ***For Windows users, simply omit the sudo command.***

***Utilize additional Docker commands*** for container management (view, start, stop, remove, etc.) are also provided in `commands-docker.txt`.

***Alternatively, if connecting directly to a locally installed PostgreSQL, you can bypass the Docker setup for the database.***

---

### API Documentation

Interactive API documentation is available via Swagger UI. Once the application is running, navigate to URL:

- http://localhost:8080/swagger-ui/index.html

#### Available Endpoints

- **Books**
  - `GET /api/books`: Retrieve all books.
  - `GET /api/books/{id}`: Retrieve a specific book by ID.
  - `POST /api/books`: Create a new book.
  - `PUT /api/books/{id}`: Update a specific book by ID.
  - `DELETE /api/books/{id}`: Delete a specific book by ID.

- **Authors**
  - `GET /api/authors`: Retrieve all authors.
  - `GET /api/authors/{id}`: Retrieve a specific author by ID.
  - `POST /api/authors`: Create a new author.
  - `PUT /api/authors/{id}`: Update a specific author by ID.
  - `DELETE /api/authors/{id}`: Delete a specific author by ID.

- **Users**
  - `GET /api/users`: Retrieve all users.
  - `GET /api/users/{id}`: Retrieve a specific user by ID.
  - `POST /api/users`: Create a new user.
  - `PUT /api/users/{id}`: Update a specific user by ID.
  - `DELETE /api/users/{id}`: Delete a specific user by ID.

- **Client**
  - `POST /api/client`: Register a new client for OAuth2 authentication.

- **Authentication**
  - `POST /api/auth/login`: User login using JWT.
  - `POST /api/auth/register`: Register a new user.

#### OAuth2 Endpoints (Optional for Google Login)
- `POST /api/oauth2/authorize`: Initiates the OAuth2 authorization process.
- `POST /api/oauth2/callback`: Handles the callback from the OAuth2 provider (e.g., Google).

### Test and Consume the API via Postman Agent

To test and consume the API, you can use the Postman Agent. It is an easy-to-use tool for testing RESTful APIs.

1.  Install Postman from Postman.

2. Import the Collection:
   - Import the Postman collection for this project into your Postman app.

   - Click the Postman icon and select Import. You can either upload a file or use a URL to import the collection.

3. Authenticate (if necessary):

   - For endpoints requiring authentication, first login with JWT by sending a POST request to `/api/auth/login`.
   - Use the returned JWT token in the authorization header for subsequent requests.

4. Start testing the endpoints by making requests to the URLs specified in the API documentation.
Additionally, the Postman Collection can be accessed via this link.

---

### Contributing

Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a new branch (git checkout -b feature/your-feature).
3. Commit your changes (git commit -m 'Add new feature').
3. Push your branch (git push origin feature/your-feature).
4. Open a pull request.

---

### License 
This project is open-source licensed, view details in file `LICENSE`.

---

### Contact
 - ***E-mail:*** joaoleal98@outlook.com

---

### Acknowledgements

I would like to express my deepest gratitude to:

- ***Spring Boot & Spring Security:*** For providing a powerful and flexible framework.
- ***PostgreSQL:*** Reliable open-source relational database.
- ***Swagger/OpenAPI:*** For interactive and comprehensive API documentation.
- ***Docker:*** For simplifying deployment and containerization.
- ***Open Source Community:*** For continuous support and contributions.

Special thanks to:
- **My mother, Tania Leal**, for her endless support and encouragement.
- **My brother, Carlos Leal**, a Machine Learning specialist, for his guidance and inspiration. You can find his GitHub at [Carlos Leal GitHub](https://github.com/carloselcastro).
- **Professor Dougllas Sousa**, for being an exceptional teacher in the Spring Boot Expert course, which greatly contributed to the development of this project. You can find his GitHub at [Dougllas Sousa GitHub](https://github.com/cursodsousa/).


