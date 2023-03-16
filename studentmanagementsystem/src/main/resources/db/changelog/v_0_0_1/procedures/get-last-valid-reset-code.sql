--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `getLastValidResetCode` (IN `studentIdIN` INT, OUT `resetCodeOUT` VARCHAR(255))  BEGIN
DECLARE resetCodeExpiracyTime DATETIME;
DECLARE isUsed TINYINT;
SELECT `Password_reset`.`expire_date` INTO resetCodeExpiracyTime FROM `Password_reset` WHERE `Password_reset`.`student_id` = studentIdIN ORDER BY `Password_reset`.`expire_date` DESC LIMIT 1;
SELECT `Password_reset`.`is_used` INTO isUsed FROM `Password_reset` WHERE `Password_reset`.`student_id` = studentIdIN ORDER BY `Password_reset`.`expire_date` DESC LIMIT 1;
IF resetCodeExpiracyTime > NOW() AND isUsed = 0
THEN
SELECT `Password_reset`.`reset_code` INTO resetCodeOUT FROM `Password_reset` WHERE `Password_reset`.`student_id` = studentIdIN ORDER BY `Password_reset`.`expire_date` DESC LIMIT 1;
END IF;
END $$