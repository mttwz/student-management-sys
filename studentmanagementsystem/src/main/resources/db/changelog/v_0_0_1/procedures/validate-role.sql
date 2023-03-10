--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `validateRole` (IN `userIdIN` INT, IN `userRoleTypeIN` INT, OUT `isValidOUT` TINYINT)  BEGIN
SELECT `user`.`role_id` into @currentRole from `user` WHERE `user`.`id` = userIdIN;
IF @currentRole = userRoleTypeIN
THEN SET isValidOUT = 1;
ELSE SET isValidOUT = 0;
END IF;
END $$