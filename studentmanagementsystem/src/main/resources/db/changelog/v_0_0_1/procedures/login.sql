--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `login` (IN `emailIN` VARCHAR(255), IN `passwordIN` VARCHAR(255), OUT `idOUT` INT)  BEGIN
SELECT `user`.`id` into idOUT FROM `user` WHERE `user`.`email` = emailIN AND `user`.`password` = SHA2(passwordIN, 256);
END $$