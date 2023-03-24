--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;


CREATE TABLE `Password_reset` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `reset_code` varchar(255) DEFAULT NULL,
  `expire_date` datetime DEFAULT NULL,
  `is_used` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `Password_reset`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);


ALTER TABLE `Password_reset`
  ADD CONSTRAINT `password_reset_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);

