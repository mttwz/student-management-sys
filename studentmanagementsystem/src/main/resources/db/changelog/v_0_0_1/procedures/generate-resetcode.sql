--liquibase formatted sql
--changeset splitStatements:true



CREATE PROCEDURE `generateResetCode` (IN `studentIdIN` INT)  BEGIN
INSERT INTO `Password_reset`(`Password_reset`.`student_id`,`Password_reset`.`reset_code`,`Password_reset`.`expire_date`)
VALUES(studentIdIN,(SELECT LEFT(UUID(), 15)),(DATE_ADD(NOW(), INTERVAL 30 MINUTE)));
END $$