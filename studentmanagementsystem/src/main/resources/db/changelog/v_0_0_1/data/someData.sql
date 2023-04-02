--liquibase formatted sql
--changeset splitStatements:true

INSERT INTO `Role`(`id`, `role_type`) VALUES (1,'superadmin');
INSERT INTO `Role`(`id`, `role_type`) VALUES (2,'admin');
INSERT INTO `Role`(`id`, `role_type`) VALUES (3,'student');

INSERT INTO `User` (`id`, `role_id`, `first_name`, `last_name`, `phone`, `birth`, `email`, `password`, `registered_at`, `activation_code`, `is_activated`, `activated_at`, `is_deleted`, `deleted_at`) VALUES (1, 1, 'superadmin', 'superadmin', 'superadmin', '1111-11-11', 'superadmin', '186cf774c97b60a1c106ef718d10970a6a06e06bef89553d9ae65d938a886eae', '2023-03-14 08:50:31', 'e5286fda', '1', NULL, '0', NULL);
INSERT INTO `User` (`id`, `role_id`, `first_name`, `last_name`, `phone`, `birth`, `email`, `password`, `registered_at`, `activation_code`, `is_activated`, `activated_at`, `is_deleted`, `deleted_at`) VALUES (2, 2, 'admin', 'admin', 'admin', '1111-11-11', 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '2023-03-14 08:50:31', 'e5286fda', '1', NULL, '0', NULL);
INSERT INTO `User` (`id`, `role_id`, `first_name`, `last_name`, `phone`, `birth`, `email`, `password`, `registered_at`, `activation_code`, `is_activated`, `activated_at`, `is_deleted`, `deleted_at`) VALUES (3, 3, 'student', 'student', 'student', '1111-11-11', 'student', '264c8c381bf16c982a4e59b0dd4c6f7808c51a05f64c35db42cc78a2a72875bb', '2023-03-14 10:24:21', '00a794f4', '1', NULL, '0', NULL);

INSERT INTO `Card` (`id`, `hash`,`created_at`,`is_deleted`,`deleted_at`,`is_assigned`,`last_assigned_to`) VALUES (1, 'haaaaash', '1111-11-11 08:08:08', 0, null, 1, 1);
INSERT INTO `Student` (`id`, `card_id`, `user_id`) VALUES (1, 1, 2);

INSERT INTO `Workgroup` (`id`, `group_name`, `institution`) VALUES (1, 'groupname', 'inst');

INSERT INTO `Workgroup_members` (`id`, `user_id`, `workgroup_id`) VALUES (1, 2, 1);