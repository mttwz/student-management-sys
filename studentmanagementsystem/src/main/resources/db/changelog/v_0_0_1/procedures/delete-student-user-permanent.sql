--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `deleteStudentUserPermanent` (IN `studentIdIN` INT)  BEGIN

DECLARE cardId int;
DECLARE userId int;
SELECT `Student`.`card_id` INTO cardId from Student WHERE `Student`.`id` = studentIdIN;

SELECT `Student`.`user_id` INTO userId from Student WHERE `Student`.`id` = studentIdIN;

UPDATE `Student` SET `Student`.`card_id` = NULL WHERE `Student`.`id` = studentIdIN;

UPDATE `Student` SET `Student`.`user_id` = NULL WHERE `Student`.`id` = studentIdIN;

DELETE FROM `Card` WHERE `Card`.`id` = cardId;
DELETE FROM `User` WHERE `User`.`id` = userId;
DELETE FROM `Student` WHERE `Student`.`id` = studentIdIN;
END $$