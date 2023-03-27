--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;


CREATE TABLE `Student` (
  `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `card_id` int(11) DEFAULT NULL UNIQUE KEY,
  `user_id` int(11) DEFAULT NULL UNIQUE KEY,
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`card_id`) REFERENCES `Card` (`id`),
  CONSTRAINT `student_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`)
);
