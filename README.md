# Running process

This README provides instructions on how to run your Spring Boot application using different methods, including command-line execution and Docker. It also includes steps for generating the database schema using Liquibase.

## Prerequisites
Before proceeding with the following steps, ensure that you have the following prerequisites installed on your system:
- Java version 18 or higher
- Maven build tool
- MySQL database server (for local development)
- Docker (if you prefer running the application using Docker)

## Running from Command Line

### Mac and Linux
1. Open a terminal.
2. Navigate to the root directory of your Spring Boot application.
3. Run the following command:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.datasource.url=jdbc:mysql://host:port/student_management_liquibase?createDatabaseIfNotExist=true"
   ```
   Replace `host` and `port` with the appropriate values for your MySQL database server.

### Windows
1. Open a command prompt.
2. Navigate to the root directory of your Spring Boot application.
3. Run the following command:
   ```bash
   mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.datasource.url=jdbc:mysql://host:port/student_management_liquibase?createDatabaseIfNotExist=true"
   ```
   Replace `host` and `port` with the appropriate values for your MySQL database server.

Note: If you're running the application from an IDE, make sure to override the `MYSQL_HOST` and `MYSQL_PORT` environment variables accordingly.

## Running with Docker

1. Open a terminal or command prompt.
2. Navigate to the main directory (outside the project directory) where the Docker Compose file is located.
3. Build the Docker image by running the following command:
   ```bash
   docker-compose build
   ```
4. Start the application using Docker Compose:
   ```bash
   docker-compose up
   ```

This will create and run a Docker container with the Spring Boot application and a MySQL database. 

Note: Yout __DON'T HAVE TO__ set the `host` and `port` values since the default values set to be used from docker.

## Conclusion
By following the steps provided above, you should be able to successfully run your Spring Boot application using either the command-line method, Docker or even from your favourite IDE.


