# Spring Boot Application with PostgreSQL, pgAdmin, Docker Compose, and Swagger


This is a sample Spring Boot application that uses PostgreSQL as the database, pgAdmin for database administration, Docker Compose for container orchestration, and Swagger for API documentation.

## Build and Run Instructions

### Prerequisites

* Docker and Docker Compose should be installed on your machine.

### Steps

1. Clone the repository or download the source code.

2. Open a terminal and navigate to the project directory.

3. Build the Docker image for the Spring Boot application using the following command:
   `docker compose build`
4. Start the containers using Docker Compose:
   `docker compose up -d`
5. This will create and configure the containers for PostgreSQL, pgAdmin, and the Spring Boot application.
6. Once the containers are running, you can access the Spring Boot application at http://localhost:8080. 
   The Swagger documentation will be available at http://localhost:8080/swagger-ui.html.

7. To access pgAdmin, open your web browser and go to http://localhost:5050. Login with the following credentials:
   * Email: admin@gmail.com
   * Password: pass

8. In pgAdmin, you can add a new server and connect to the PostgreSQL container using the following details:

* Hostname/address: pg_db_test  (or IP address. In our case it is the container_name of postgres)
* Port: 5432
* Username: postgres
* Password: pass

9. You can now interact with the Spring Boot application and the PostgreSQL database through the exposed API endpoints.

### API Endpoints

The following API endpoints are available:

* GET /api/v1/users: Get all users.
* GET /api/v1/users/{id}: Get a user by ID.
* POST /api/v1/users: Create a new user.
* PUT /api/v1/users/{id}: Update a user by ID.
* DELETE /api/v1/users/{id}: Delete a user by ID.

Make sure to replace {id} with the actual ID of the user when using the specific endpoints.