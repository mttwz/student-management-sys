--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `register` (IN `roleIdIn` INT, IN `firstNameIN` VARCHAR(255), IN `lastNameIN` VARCHAR(255), IN `phoneIN` VARCHAR(255), IN `birthIN` DATE, IN `emailIN` VARCHAR(255), IN `passwordIN` VARCHAR(255), OUT `idOUT` INT)  BEGIN
INSERT INTO `User` ( `User`.`role_id` ,`User`.`first_name`,`User`.`last_name`,`User`.`phone`,`User`.`birth`,`User`.`email`,`User`.`password`,`User`.`activation_code`)
VALUES(roleIdIn,firstNameIN,lastNameIN,phoneIN,birthIN,emailIN, SHA2(passwordIN, 256), (SELECT LEFT(UUID(), 8)));
SET idOUT = LAST_INSERT_ID();
END $$