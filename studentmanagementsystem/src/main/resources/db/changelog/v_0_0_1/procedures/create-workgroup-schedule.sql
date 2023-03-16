--liquibase formatted sql
--changeset splitStatements:true


CREATE PROCEDURE `createWorkgroupSchedule` (IN `nameIN` VARCHAR(255), IN `startIN` DATETIME, IN `endIN` DATETIME, IN `isOnsiteIN` BOOLEAN, IN `workgroupIdIN` INT, OUT `idOUT` INT)  BEGIN
INSERT INTO `workgroup_schedule`(`workgroup_schedule`.`name`,`workgroup_schedule`.`workgroup_id`,`workgroup_schedule`.`start`,`workgroup_schedule`.`end`,`workgroup_schedule`.`is_onsite`)
VALUES(nameIN,workgroupIdIN,startIN,endIN,isOnsiteIN);
SET idOUT = LAST_INSERT_ID();
END $$