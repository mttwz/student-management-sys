--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;


CREATE TABLE `Student` (
  `id` int(11) NOT NULL,
  `card_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
);


ALTER TABLE `Student`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `card_id` (`card_id`),
  ADD UNIQUE KEY `user_id` (`user_id`);

ALTER TABLE `Student`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;


ALTER TABLE `Student`
  ADD CONSTRAINT `student_ibfk_1` FOREIGN KEY (`card_id`) REFERENCES `Card` (`id`),
  ADD CONSTRAINT `student_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`);

