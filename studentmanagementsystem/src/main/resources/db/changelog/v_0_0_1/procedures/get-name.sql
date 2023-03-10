--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `getName` (IN `userIdIN` INT)  BEGIN
SELECT `user`.`first_name`, `user`.`last_name` FROM `user` WHERE `user`.`id` = userIdIN;
END $$