--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `setUserRole` (IN `userIdIN` INT, IN `roleTypeIn` VARCHAR(255))  BEGIN
UPDATE User SET `user`.`role_id` = (SELECT `role`.`id` FROM `role` WHERE `role`.`role_type` = roleTypeIn) WHERE `user`.`id` = userIdIN;
END $$