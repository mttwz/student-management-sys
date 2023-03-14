--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `getAllUserIdFromWorkgroup` (IN `workgroupIdIN` INT)  BEGIN
SELECT `Workgroup_members`.`user_id` FROM `Workgroup_members` WHERE `Workgroup_members`.`workgroup_id` = workgroupIdIN;
END $$