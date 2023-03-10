--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `logStudent` (IN `studentIdIN` INT)  BEGIN
DECLARE lastArrival DATE;
DECLARE lastLeaving DATE;
SELECT `Attendance`.`arrival` INTO lastArrival from Attendance WHERE `Attendance`.`student_id` = studentIdIN ORDER BY `Attendance`.`arrival` DESC LIMIT 1;
SELECT `Attendance`.`leaving` INTO lastLeaving from Attendance WHERE `Attendance`.`student_id` = studentIdIN ORDER BY `Attendance`.`arrival` DESC LIMIT 1;
IF lastLeaving IS NULL AND lastArrival = CURDATE()
THEN
UPDATE `Attendance` SET `Attendance`.`leaving` = NOW() WHERE `Attendance`.`student_id` = studentIdIN ORDER BY `Attendance`.`arrival` DESC LIMIT 1;
ELSE
INSERT INTO `Attendance` (`Attendance`.`student_id`,`Attendance`.`arrival`) VALUES(studentIdIN,NOW());
END IF;
END $$