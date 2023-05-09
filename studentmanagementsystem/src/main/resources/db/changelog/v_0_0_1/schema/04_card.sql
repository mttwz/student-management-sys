--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;



CREATE TABLE `card` (
  `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `hash` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `deleted_at` datetime DEFAULT NULL,
  `is_assigned` tinyint(1) NOT NULL DEFAULT '0',
  `last_assigned_to` int(11) DEFAULT NULL
);




