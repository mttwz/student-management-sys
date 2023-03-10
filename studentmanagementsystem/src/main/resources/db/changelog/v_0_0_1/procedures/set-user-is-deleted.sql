--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `setUserIsDeleted` (IN `userIdIN` INT)  BEGIN
UPDATE `User` SET `User`.`is_deleted` = TRUE WHERE `User`.`id` = userIdIN;
UPDATE `User` SET `User`.`deleted_at` = NOW() WHERE `User`.`id` = userIdIN;
END $$