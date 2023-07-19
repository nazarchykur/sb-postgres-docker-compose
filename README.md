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

## Code Coverage with JaCoCo

This project utilizes JaCoCo for code coverage analysis. JaCoCo is a Java code coverage library that helps measure the extent to which your code is tested. It provides insights into which parts of your codebase are covered by tests and identifies areas that need additional testing.

###  Generating the JaCoCo Report
To generate the code coverage report using JaCoCo and verify the coverage thresholds, follow these steps:

1. Run the JaCoCo report task: Open a terminal or command prompt and navigate to your project directory. Then, execute the following command: `./gradlew jacocoTestReport`

   This command will run your tests and generate the JaCoCo report.

2. Access the JaCoCo report: After running the jacocoTestReport task, you can access the generated JaCoCo report by opening the HTML file located in the `build/jacocoHtml/index.html`. This report provides an overview of the code coverage, highlighting which parts of the codebase are covered by tests.

3. Verify the coverage thresholds: To ensure that the code coverage meets the defined thresholds, run the following command: `./gradlew jacocoTestCoverageVerification`

   This command will verify the coverage against the configured thresholds and provide feedback on whether the coverage meets the requirements.

###  Understanding the JaCoCo Report
The JaCoCo report provides detailed information about code coverage, including metrics such as line coverage, branch coverage, and complexity metrics. It highlights the specific lines of code that are covered or not covered by tests, allowing you to identify areas that require additional testing.

![img_8.png](src%2Fmain%2Fresources%2Fscreenshots%2Fimg_8.png)