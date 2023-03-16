--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `updateJwt` (IN `userIdIN` INT, IN `jwtIN` TEXT)  BEGIN
UPDATE `User` SET `User`.`jwt` = jwtIN WHERE `User`.`id` = userIdIN;
UPDATE `User` SET `User`.`jwt_expire_date` = DATE_ADD(now(), INTERVAL 7 DAY) WHERE `User`.`id` = userIdIN;
END $$