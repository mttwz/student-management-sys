--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `setUserIsActivated` (IN `userIdIN` INT)  BEGIN
UPDATE `User` SET `User`.`is_activated` = true WHERE `User`.`id` = userIdIN;
UPDATE `User` SET `User`.`activated_at` = NOW() WHERE `User`.`id` = userIdIN;
END $$