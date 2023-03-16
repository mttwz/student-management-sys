--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `createCard` (IN `hashIN` VARCHAR(255), OUT `idOUT` INT)  BEGIN
INSERT INTO Card SET `card`.`hash` = hashIN;
SET idOUT = LAST_INSERT_ID();
END $$
