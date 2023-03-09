--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;

CREATE TABLE `Attendance` (
  `id` int(11) NOT NULL,
  `student_id` int(11) DEFAULT NULL,
  `arrival` datetime DEFAULT NULL,
  `leaving` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `Attendance`
  ADD PRIMARY KEY (`id`),
  ADD KEY `student_id` (`student_id`);

ALTER TABLE `Attendance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;


ALTER TABLE `Attendance`
  ADD CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
