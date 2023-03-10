--liquibase formatted sql
--changeset splitStatements:true


CREATE PROCEDURE `connectStudentToUser` (IN `studentIdIN` INT, IN `userIdIN` INT)  BEGIN
UPDATE Student SET `student`.`user_id` = userIdIN WHERE `student`.`id` = studentIdIN;
END $$