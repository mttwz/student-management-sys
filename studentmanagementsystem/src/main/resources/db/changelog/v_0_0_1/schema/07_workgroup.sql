--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;



CREATE TABLE `workgroup` (
  `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `group_name` varchar(255) DEFAULT NULL,
  `institution` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `deleted_at` datetime DEFAULT NULL
);