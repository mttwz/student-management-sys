--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;




CREATE TABLE `workgroup_members` (
  `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `workgroup_id` int(11) DEFAULT NULL,
  CONSTRAINT `workgroup_members_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `workgroup_members_ibfk_2` FOREIGN KEY (`workgroup_id`) REFERENCES `workgroup` (`id`)
);

