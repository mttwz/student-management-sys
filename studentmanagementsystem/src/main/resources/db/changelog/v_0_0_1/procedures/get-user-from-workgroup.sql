--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `getUserFromWorkgroup` (IN `userIdIn` INT)  BEGIN
SELECT `User`.`id`,`User`.`role_id`,`User`.`first_name`,`User`.`last_name`
,`User`.`phone`, `User`.`birth`, `User`.`email`, `User`.`registered_at`, `User`.`is_activated`, `User`.`is_deleted`, `Workgroup`.`group_name`, `Workgroup`.`institution`
FROM `Workgroup`
LEFT JOIN `Workgroup_members`
ON `Workgroup`.`id` = `Workgroup_members`.`workgroup_id`
LEFT JOIN `User`
ON `User`.`id` = `Workgroup_members`.`user_id`
WHERE `User`.`id` = userIdIn;
END $$