--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `addUserToWorkgroup` (IN `userIdIN` INT, IN `workgroupIdIN` INT, OUT `idOUT` INT)  BEGIN
INSERT INTO `Workgroup_members` (`Workgroup_members`.`user_id`,`Workgroup_members`.`workgroup_id`) VALUES(userIdIN,workgroupIdIN);
SET idOUT = LAST_INSERT_ID();
END $$
