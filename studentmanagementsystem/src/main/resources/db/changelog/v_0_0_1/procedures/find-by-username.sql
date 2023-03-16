--liquibase formatted sql
--changeset splitStatements:true


CREATE PROCEDURE `findByUsername` (IN `userNameIN` VARCHAR(255))  BEGIN
SELECT * FROM `user` WHERE `user`.`email` = userNameIN;
END $$