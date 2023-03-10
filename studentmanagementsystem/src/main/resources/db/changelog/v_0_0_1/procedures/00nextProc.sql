--liquibase formatted sql
--changeset splitStatements:true



CREATE PROCEDURE `generateResetCode` (IN `studentIdIN` INT)  BEGIN
INSERT INTO `Password_reset`(`Password_reset`.`student_id`,`Password_reset`.`reset_code`,`Password_reset`.`expire_date`)
VALUES(studentIdIN,(SELECT LEFT(UUID(), 15)),(DATE_ADD(NOW(), INTERVAL 30 MINUTE)));
END $$

CREATE PROCEDURE `getAllStudent` ()  BEGIN
SELECT `User`.`id`, `User`.`first_name`,`User`.`last_name`,`User`.`birth`,`User`.`phone`,`User`.`email`, `User`.`registered_at`,`User`.`is_activated`,User.activated_at,`User`.`is_deleted`,`User`.`deleted_at` FROM `Student`
LEFT JOIN `User` ON `Student`.`user_id` = `User`.`id`;
END $$

CREATE PROCEDURE `getAllUser` ()  BEGIN
SELECT `User`.`id`, `User`.`role_id`,`User`.`first_name`,`User`.`last_name`,`User`.`birth`,`User`.`phone`,`User`.`email`, `User`.`registered_at`,`User`.`is_activated`,User.activated_at,`User`.`is_deleted`,`User`.`deleted_at` FROM `User`;
END $$

CREATE PROCEDURE `getAllUsertIdFromWorkgroup` (IN `workgroupIdIN` INT)  BEGIN
SELECT `Workgroup_members`.`user_id` FROM `Workgroup_members` WHERE `Workgroup_members`.`workgroup_id` = workgroupIdIN;
END $$

CREATE PROCEDURE `getAllWorkgroupClassesIdOnDate` (IN `workgroupIdIN` INT, IN `dateIn` DATE)  BEGIN
SELECT `Workgroup_schedule`.`id`,`Workgroup_schedule`.`name` FROM `Workgroup_schedule` WHERE `Workgroup_schedule`.`workgroup_id` = workgroupIdIN AND CAST(`Workgroup_schedule`.`start` AS DATE)= dateIn;
END $$

CREATE PROCEDURE `getLastValidResetCode` (IN `studentIdIN` INT, OUT `resetCodeOUT` VARCHAR(255))  BEGIN
DECLARE resetCodeExpiracyTime DATETIME;
DECLARE isUsed TINYINT;
SELECT `Password_reset`.`expire_date` INTO resetCodeExpiracyTime FROM `Password_reset` WHERE `Password_reset`.`student_id` = studentIdIN ORDER BY `Password_reset`.`expire_date` DESC LIMIT 1;
SELECT `Password_reset`.`is_used` INTO isUsed FROM `Password_reset` WHERE `Password_reset`.`student_id` = studentIdIN ORDER BY `Password_reset`.`expire_date` DESC LIMIT 1;
IF resetCodeExpiracyTime > NOW() AND isUsed = 0
THEN
SELECT `Password_reset`.`reset_code` INTO resetCodeOUT FROM `Password_reset` WHERE `Password_reset`.`student_id` = studentIdIN ORDER BY `Password_reset`.`expire_date` DESC LIMIT 1;
END IF;
END $$

CREATE PROCEDURE `getName` (IN `userIdIN` INT)  BEGIN
SELECT `user`.`first_name`, `user`.`last_name` FROM `user` WHERE `user`.`id` = userIdIN;
END $$

CREATE PROCEDURE `getUserCard` (IN `userIdIN` INT, OUT `cardIdOUT` INT)  BEGIN
SELECT `Student`.`card_id` INTO cardIdOUT FROM `Student` WHERE `Student`.`user_id` = userIdIN;
END $$

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

CREATE PROCEDURE `getUserIdByToken` (IN `tokenIN` VARCHAR(255), OUT `userIdOUT` INT)  BEGIN
SELECT `user`.`id` into userIdOUT FROM `user` WHERE `user`.`jwt` = tokenIN;
END $$

CREATE PROCEDURE `getWorkgroupScheduleByUserId` (IN `userIdIN` INT)  BEGIN
SELECT `Workgroup_schedule`.`id`FROM `Workgroup_members`
CROSS JOIN `Workgroup_schedule` on `Workgroup_members`.`workgroup_id` = `Workgroup_schedule`.`workgroup_id`
CROSS JOIN `Workgroup` on `Workgroup_members`.`workgroup_id` = `Workgroup`.`id`
WHERE `Workgroup_members`.`user_id` = userIdIN;
END $$

CREATE PROCEDURE `logStudent` (IN `studentIdIN` INT)  BEGIN
DECLARE lastArrival DATE;
DECLARE lastLeaving DATE;
SELECT `Attendance`.`arrival` INTO lastArrival from Attendance WHERE `Attendance`.`student_id` = studentIdIN ORDER BY `Attendance`.`arrival` DESC LIMIT 1;
SELECT `Attendance`.`leaving` INTO lastLeaving from Attendance WHERE `Attendance`.`student_id` = studentIdIN ORDER BY `Attendance`.`arrival` DESC LIMIT 1;
IF lastLeaving IS NULL AND lastArrival = CURDATE()
THEN
UPDATE `Attendance` SET `Attendance`.`leaving` = NOW() WHERE `Attendance`.`student_id` = studentIdIN ORDER BY `Attendance`.`arrival` DESC LIMIT 1;
ELSE
INSERT INTO `Attendance` (`Attendance`.`student_id`,`Attendance`.`arrival`) VALUES(studentIdIN,NOW());
END IF;
END $$

CREATE PROCEDURE `register` (IN `roleIdIn` INT, IN `firstNameIN` VARCHAR(255), IN `lastNameIN` VARCHAR(255), IN `phoneIN` VARCHAR(255), IN `birthIN` DATE, IN `emailIN` VARCHAR(255), IN `passwordIN` VARCHAR(255), OUT `idOUT` INT)  BEGIN
INSERT INTO `User` ( `User`.`role_id` ,`User`.`first_name`,`User`.`last_name`,`User`.`phone`,`User`.`birth`,`User`.`email`,`User`.`password`,`User`.`activation_code`)
VALUES(roleIdIn,firstNameIN,lastNameIN,phoneIN,birthIN,emailIN, SHA2(passwordIN, 256), (SELECT LEFT(UUID(), 8)));
SET idOUT = LAST_INSERT_ID();
END $$

CREATE PROCEDURE `registerStudent` (IN `firstNameIN` VARCHAR(255), IN `lastNameIN` VARCHAR(255), IN `phoneIN` VARCHAR(255), IN `birthIN` DATE, IN `emailIN` VARCHAR(255), IN `passwordIN` VARCHAR(255), OUT `idOUT` INT)  BEGIN
-- DECLARE studentID int;
-- DECLARE userID int;
-- INSERT INTO `Student` (`Student`.`first_name`,`Student`.`last_name`,`Student`.`phone`,`Student`.`birth`)
-- VALUES(firstNameIN,lastNameIN,phoneIN,birthIN);
-- SET studentID = LAST_INSERT_ID();
-- INSERT INTO `User` (`User`.`email`,`User`.`password`,`User`.`reg_code`,`User`.`is_activated`,`User`.`is_deleted`)
-- VALUES(emailIN,(SHA2(CONCAT(passwordIN), 256)), (SELECT LEFT(UUID(), 8)),FALSE,FALSE);
-- SET userID = LAST_INSERT_ID();
CALL register(3,firstNameIN,lastNameIN,phoneIN,birthIN,emailIN,passwordIN,@userId);
call setUserRole(@userId,"Student");
call createStudentWithUserId(@userId);
SET idOUT = @userId;
END $$

CREATE PROCEDURE `searchAdmins` (IN `searchIN` VARCHAR(255))  BEGIN
SELECT * FROM `user` WHERE (`User`.`first_name` LIKE concat("%",searchIN,"%") or `User`.`last_name` LIKE concat("%",searchIN,"%") or `User`.`email` LIKE concat("%",searchIN,"%") or `User`.`birth` LIKE concat("%",searchIN,"%")) and `User`.`role_id` =2 ;
END $$

CREATE PROCEDURE `searchAllUser` (IN `searchIN` VARCHAR(255))  BEGIN
SELECT * FROM `User` WHERE `User`.`first_name` LIKE concat("%",searchIN,"%") or `User`.`last_name` LIKE concat("%",searchIN,"%") or `User`.`email` LIKE concat("%",searchIN,"%")or `User`.`birth` LIKE concat("%",searchIN,"%");
END $$

CREATE PROCEDURE `searchStudents` (IN `searchIN` VARCHAR(255))  BEGIN
SELECT * FROM `user` WHERE (`User`.`first_name` LIKE concat("%",searchIN,"%") or `User`.`last_name` LIKE concat("%",searchIN,"%") or `User`.`email` LIKE concat("%",searchIN,"%") or `User`.`birth` LIKE concat("%",searchIN,"%")) and `User`.`role_id` = 3;
END $$

CREATE PROCEDURE `searchSuperadmins` (IN `searchIN` VARCHAR(255))  BEGIN
SELECT * FROM `user` WHERE (`User`.`first_name` LIKE concat("%",searchIN,"%") or `User`.`last_name` LIKE concat("%",searchIN,"%") or `User`.`email` LIKE concat("%",searchIN,"%") or `User`.`birth` LIKE concat("%",searchIN,"%")) and `User`.`role_id` = 1;
END $$

CREATE PROCEDURE `searchWorkgroups` (IN `searchIN` VARCHAR(255))  BEGIN
SELECT * FROM `User`
JOIN `Workgroup_members`
ON `User`.`id` = `Workgroup_members`.`user_id`
JOIN `Workgroup`
ON `Workgroup`.`id` = `Workgroup_members`.`workgroup_id`
WHERE `User`.`first_name` LIKE concat("%",searchIN,"%") or `User`.`last_name` LIKE concat("%",searchIN,"%") or `User`.`email` LIKE concat("%",searchIN,"%") or `User`.`birth` LIKE concat("%",searchIN,"%");
END $$

CREATE PROCEDURE `setUserIsActivated` (IN `userIdIN` INT)  BEGIN
UPDATE `User` SET `User`.`is_activated` = true WHERE `User`.`id` = userIdIN;
UPDATE `User` SET `User`.`activated_at` = NOW() WHERE `User`.`id` = userIdIN;
END $$

CREATE PROCEDURE `setUserIsDeleted` (IN `userIdIN` INT)  BEGIN
UPDATE `User` SET `User`.`is_deleted` = TRUE WHERE `User`.`id` = userIdIN;
UPDATE `User` SET `User`.`deleted_at` = NOW() WHERE `User`.`id` = userIdIN;
END $$

CREATE PROCEDURE `setUserRole` (IN `userIdIN` INT, IN `roleTypeIn` VARCHAR(255))  BEGIN
UPDATE User SET `user`.`role_id` = (SELECT `role`.`id` FROM `role` WHERE `role`.`role_type` = roleTypeIn) WHERE `user`.`id` = userIdIN;
END $$

CREATE PROCEDURE `updateJwt` (IN `userIdIN` INT, IN `jwtIN` TEXT)  BEGIN
UPDATE `User` SET `User`.`jwt` = jwtIN WHERE `User`.`id` = userIdIN;
UPDATE `User` SET `User`.`jwt_expire_date` = DATE_ADD(now(), INTERVAL 7 DAY) WHERE `User`.`id` = userIdIN;
END $$

CREATE PROCEDURE `validateJwt` (IN `userIdIN` INT, IN `jwtIN` VARCHAR(255), OUT `isValidOUT` TINYINT)  BEGIN
SELECT `user`.`jwt` into @currentJwt from `user` WHERE `user`.`id` = userIdIN;
IF @currentJwt = jwtIN
THEN SET isValidOUT = 1;
ELSE SET isValidOUT = 0;
END IF;
END $$

CREATE PROCEDURE `validateRole` (IN `userIdIN` INT, IN `userRoleTypeIN` INT, OUT `isValidOUT` TINYINT)  BEGIN
SELECT `user`.`role_id` into @currentRole from `user` WHERE `user`.`id` = userIdIN;
IF @currentRole = userRoleTypeIN
THEN SET isValidOUT = 1;
ELSE SET isValidOUT = 0;
END IF;
END $$

CREATE PROCEDURE `valtozoEltarolasaTeszt` ()  BEGIN
-- DECLARE asd int;
call createStudent("fn","ln","ph","1999.11.11",@asd);
SELECT @asd;
END $$