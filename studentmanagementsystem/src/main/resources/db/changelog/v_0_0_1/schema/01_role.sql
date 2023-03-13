--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;

CREATE TABLE `Role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_type` varchar(255) DEFAULT NULL,
  CONSTRAINT PK_QSD PRIMARY KEY  (`id`)
);
