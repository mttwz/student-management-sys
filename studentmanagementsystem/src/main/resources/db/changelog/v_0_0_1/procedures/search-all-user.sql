--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `searchAllUser` (IN `searchIN` VARCHAR(255))  BEGIN
SELECT * FROM `User` WHERE `User`.`first_name` LIKE concat("%",searchIN,"%") or `User`.`last_name` LIKE concat("%",searchIN,"%") or `User`.`email` LIKE concat("%",searchIN,"%")or `User`.`birth` LIKE concat("%",searchIN,"%");
END $$