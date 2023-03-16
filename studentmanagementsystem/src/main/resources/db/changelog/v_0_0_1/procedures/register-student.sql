--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `registerStudent` (IN `firstNameIN` VARCHAR(255), IN `lastNameIN` VARCHAR(255), IN `phoneIN` VARCHAR(255), IN `birthIN` DATE, IN `emailIN` VARCHAR(255), IN `passwordIN` VARCHAR(255), OUT `idOUT` INT)  BEGIN
CALL register(3,firstNameIN,lastNameIN,phoneIN,birthIN,emailIN,passwordIN,@userId);
call setUserRole(@userId,"Student");
call createStudentWithUserId(@userId);
SET idOUT = @userId;
END $$