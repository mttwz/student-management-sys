--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;

CREATE TABLE `Role` (
  `id` int(11) NOT NULL,
  `role_type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `Role`
  ADD PRIMARY KEY (`id`);


ALTER TABLE `Role`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;