--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;

CREATE TABLE `User` (
  `id` int(11) NOT NULL,
  `role_id` int(11) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `birth` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `registered_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `activation_code` text,
  `is_activated` tinyint(1) NOT NULL DEFAULT '0',
  `activated_at` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `User`
  ADD PRIMARY KEY (`id`),
  ADD KEY `role_id` (`role_id`);


ALTER TABLE `User`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;


ALTER TABLE `User`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `Role` (`id`);


