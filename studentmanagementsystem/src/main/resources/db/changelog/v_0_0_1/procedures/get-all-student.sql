--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `getAllStudent` ()  BEGIN
SELECT `User`.`id`, `User`.`first_name`,`User`.`last_name`,`User`.`birth`,`User`.`phone`,`User`.`email`, `User`.`registered_at`,`User`.`is_activated`,User.activated_at,`User`.`is_deleted`,`User`.`deleted_at` FROM `Student`
LEFT JOIN `User` ON `Student`.`user_id` = `User`.`id`;
END $$