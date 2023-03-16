--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `getAllWorkgroupClassesIdOnDate`(IN `workgroupIdIN` INT, IN `dateIn` DATE)
BEGIN
SELECT `Workgroup_schedule`.`id`, `Workgroup_schedule`.`name`
FROM `Workgroup_schedule`
WHERE `Workgroup_schedule`.`workgroup_id` = workgroupIdIN
  AND CAST(`Workgroup_schedule`.`start` AS DATE) = dateIn;
END $$