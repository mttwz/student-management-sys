--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;

CREATE TABLE `attendance` (
  `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `student_id` int(11) DEFAULT NULL,
  `arrival` datetime DEFAULT NULL,
  `leaving` datetime DEFAULT NULL,
  CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)
);
