--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;



CREATE TABLE `Card` (
  `id` int(11) NOT NULL,
  `hash` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `Card`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `Card`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;




