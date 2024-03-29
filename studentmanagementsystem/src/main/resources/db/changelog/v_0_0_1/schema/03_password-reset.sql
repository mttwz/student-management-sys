--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;


CREATE TABLE `password_reset` (
  `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `reset_code` varchar(255) DEFAULT NULL,
  `expire_date` datetime DEFAULT NULL,
  `is_used` tinyint(1) NOT NULL DEFAULT '0',
  CONSTRAINT `password_reset_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);




