--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;



CREATE TABLE `Workgroup` (
  `id` int(11) NOT NULL,
  `group_name` varchar(255) DEFAULT NULL,
  `institution` varchar(255) DEFAULT NULL,
    `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) DEFAULT '0',
  `deleted_at` datetime DEFAULT NULL
);


ALTER TABLE `Workgroup`
  ADD PRIMARY KEY (`id`);


ALTER TABLE `Workgroup`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;


