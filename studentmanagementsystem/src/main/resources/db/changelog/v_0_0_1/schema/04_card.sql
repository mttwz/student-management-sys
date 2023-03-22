--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;



CREATE TABLE `Card` (
  `id` int(11) NOT NULL,
  `hash` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `deleted_at` datetime DEFAULT NULL,
  `is_assigned` tinyint(1) NOT NULL DEFAULT '0',
  `assigned_to` int(11) DEFAULT NULL
);


ALTER TABLE `Card`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `Card`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;




