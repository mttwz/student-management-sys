--liquibase formatted sql
--changeset splitStatements:true


CREATE PROCEDURE `createWorkgroup` (IN `groupNameIN` VARCHAR(255), IN `institutionIN` VARCHAR(255), OUT `idOUT` INT)  BEGIN
INSERT INTO `Workgroup` (`Workgroup`.`group_name`,`Workgroup`.`institution`)
VALUES(groupNameIN,institutionIN);
SET idOUT = LAST_INSERT_ID();
END $$