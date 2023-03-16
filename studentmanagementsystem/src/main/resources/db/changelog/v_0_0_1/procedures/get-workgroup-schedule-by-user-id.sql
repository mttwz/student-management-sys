--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `getWorkgroupScheduleByUserId` (IN `userIdIN` INT)  BEGIN
SELECT `Workgroup_schedule`.`id`FROM `Workgroup_members`
CROSS JOIN `Workgroup_schedule` on `Workgroup_members`.`workgroup_id` = `Workgroup_schedule`.`workgroup_id`
CROSS JOIN `Workgroup` on `Workgroup_members`.`workgroup_id` = `Workgroup`.`id`
WHERE `Workgroup_members`.`user_id` = userIdIN;
END $$