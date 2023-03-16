--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `searchWorkgroups` (IN `searchIN` VARCHAR(255))  BEGIN
SELECT * FROM `User`
JOIN `Workgroup_members`
ON `User`.`id` = `Workgroup_members`.`user_id`
JOIN `Workgroup`
ON `Workgroup`.`id` = `Workgroup_members`.`workgroup_id`
WHERE `User`.`first_name` LIKE concat("%",searchIN,"%") or `User`.`last_name` LIKE concat("%",searchIN,"%") or `User`.`email` LIKE concat("%",searchIN,"%") or `User`.`birth` LIKE concat("%",searchIN,"%");
END $$