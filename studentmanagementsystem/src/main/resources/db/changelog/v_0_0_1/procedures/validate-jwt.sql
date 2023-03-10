--liquibase formatted sql
--changeset splitStatements:true

CREATE PROCEDURE `validateJwt` (IN `userIdIN` INT, IN `jwtIN` VARCHAR(255), OUT `isValidOUT` TINYINT)  BEGIN
SELECT `user`.`jwt` into @currentJwt from `user` WHERE `user`.`id` = userIdIN;
IF @currentJwt = jwtIN
THEN SET isValidOUT = 1;
ELSE SET isValidOUT = 0;
END IF;
END $$