--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;




CREATE TABLE `Workgroup_members` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `workgroup_id` int(11) DEFAULT NULL
);


ALTER TABLE `Workgroup_members`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `workgroup_id` (`workgroup_id`);


ALTER TABLE `Workgroup_members`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;


ALTER TABLE `Workgroup_members`
  ADD CONSTRAINT `workgroup_members_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`),
  ADD CONSTRAINT `workgroup_members_ibfk_2` FOREIGN KEY (`workgroup_id`) REFERENCES `Workgroup` (`id`);
