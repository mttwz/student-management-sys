--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `getCardByUserdId` (IN `userIdIN` INT, OUT `cardIdOUT` INT)  BEGIN
SELECT `Student`.`card_id` INTO cardIdOUT FROM `Student` WHERE `Student`.`user_id` = userIdIN;
END $$