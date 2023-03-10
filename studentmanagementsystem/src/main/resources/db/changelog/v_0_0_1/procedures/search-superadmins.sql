--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `searchSuperadmins` (IN `searchIN` VARCHAR(255))  BEGIN
SELECT * FROM `user` WHERE (`User`.`first_name` LIKE concat("%",searchIN,"%") or `User`.`last_name` LIKE concat("%",searchIN,"%") or `User`.`email` LIKE concat("%",searchIN,"%") or `User`.`birth` LIKE concat("%",searchIN,"%")) and `User`.`role_id` = 1;
END $$