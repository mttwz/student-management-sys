--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `getAllUser` ()  BEGIN
SELECT `User`.`id`, `User`.`role_id`,`User`.`first_name`,`User`.`last_name`,`User`.`birth`,`User`.`phone`,`User`.`email`, `User`.`registered_at`,`User`.`is_activated`,User.activated_at,`User`.`is_deleted`,`User`.`deleted_at` FROM `User`;
END $$