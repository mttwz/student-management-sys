--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;


CREATE TABLE `Workgroup_schedule` (
  `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `workgroup_id` int(11) DEFAULT NULL,
  `start` datetime DEFAULT NULL,
  `end` datetime DEFAULT NULL,
  `is_onsite` tinyint(1)NOT NULL DEFAULT '1',
  `is_deleted` tinyint(1)NOT NULL DEFAULT '0',
  `deleted_at` datetime DEFAULT NULL,
  CONSTRAINT `workgroup_schedule_ibfk_1` FOREIGN KEY (`workgroup_id`) REFERENCES `Workgroup` (`id`)
);