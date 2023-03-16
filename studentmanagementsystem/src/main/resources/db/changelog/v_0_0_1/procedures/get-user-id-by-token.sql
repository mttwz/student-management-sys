--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `getUserIdByToken` (IN `tokenIN` VARCHAR(255), OUT `userIdOUT` INT)  BEGIN
SELECT `user`.`id` into userIdOUT FROM `user` WHERE `user`.`jwt` = tokenIN;
END $$