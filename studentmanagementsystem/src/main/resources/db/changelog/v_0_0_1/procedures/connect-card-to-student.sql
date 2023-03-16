--liquibase formatted sql
--changeset splitStatements:true


CREATE PROCEDURE `connectCardToStudent` (IN `studentIdIN` INT, IN `cardIdIN` INT)  BEGIN
UPDATE Student SET `student`.`card_id` = cardIdIN WHERE `student`.`id` = studentIdIN;
END $$



