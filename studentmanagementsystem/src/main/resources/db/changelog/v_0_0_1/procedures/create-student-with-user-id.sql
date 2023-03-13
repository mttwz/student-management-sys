--liquibase formatted sql
--changeset splitStatements:true


CREATE PROCEDURE `createStudentWithUserId` (IN `userIdIN` INT)  BEGIN
INSERT INTO `student` (`student`.`user_id`) VALUES (userIdIN);
END $$