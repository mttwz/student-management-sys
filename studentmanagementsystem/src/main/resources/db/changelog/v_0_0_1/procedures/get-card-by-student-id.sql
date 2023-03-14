--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `getCardByStudentId` (IN `studentIdIN` INT, OUT `cardIdOUT` INT)  BEGIN
SELECT `Student`.`card_id` INTO cardIdOUT FROM `Student` WHERE `Student`.`id` = studentIdIN;
END $$