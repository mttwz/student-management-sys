# Running process

This README provides instructions on how to run this Spring Boot application using different methods, including command-line execution, Docker or any IDE. 



## Prerequisites
Before proceeding with the following steps, ensure that you have the following prerequisites installed on your system:
- Java version 18 or higher
- Maven build tool
- MySQL database server (for local development)
   :information_source: __Note:__ The database schema automatically generating itself using Liquibase.
- Docker (if you prefer running the application using Docker)

---

## Running from Command Line

:exclamation: __NOTE:__ You have to run a mysql database before the app starts!

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

:information_source: __Note:__ If you're running the application from an IDE, make sure to override the `MYSQL_HOST` and `MYSQL_PORT` environment variables accordingly.

---

## Running with Docker

### __Prepare__
### Mac and Linux
1. Open a terminal.
2. Navigate to the root directory of your Spring Boot application.
3. Run the following command:
   ```bash
   ./mvnw clean install -DskipTests
   ```

### Windows
1. Open a command prompt.
2. Navigate to the root directory of your Spring Boot application.
3. Run the following command:
   ```bash
   mvnw clean install -DskipTests
   ```
:information_source: __Note:__ These commands will build the .jar file. Optionally you can remove the -DskipTests flag, but this will __increase__ the build time!

## Running
:exclamation: If your root password other that the default `root` then you __HAVE TO__ change the `docker-compose.yml` MYSQL_ROOT_PASSWORD flag accordingly.

1. Run the docker desktop application.
2. Open a terminal or command prompt.
4. Navigate to the main directory (outside the project directory) where the Docker Compose file is located.
5. Build the Docker image by running the following command:
   ```bash
   docker-compose build
   ```
6. Start the application using Docker Compose:
   ```bash
   docker-compose up
   ```

This will create and run a Docker container with the Spring Boot application and a MySQL database. 

:warning: The application start could fail for a couple of times (restarting itself), this is a __normal__ behavior since the application can not start until the database up and running. You can ignore that.



:exclamation: Note: Yout __DON'T HAVE TO__ set the `host` and `port` values since the default values set to be used from docker.

## Conclusion
By following the steps provided above, you should be able to successfully run this Spring Boot application using either the command-line method, Docker or even from your favourite IDE :blush:.


