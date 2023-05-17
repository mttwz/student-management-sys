--liquibase formatted sql
--changeset splitStatements:true

INSERT INTO `role`(`id`, `role_type`) VALUES (1,'superadmin');
INSERT INTO `role`(`id`, `role_type`) VALUES (2,'admin');
INSERT INTO `role`(`id`, `role_type`) VALUES (3,'student');

INSERT INTO `user` (`id`, `role_id`, `first_name`, `last_name`, `phone`, `birth`, `email`, `password`, `registered_at`, `activation_code`, `is_activated`, `activated_at`, `is_deleted`, `deleted_at`)
VALUES (1, 1, 'superadmin', 'superadmin', 'superadmin', '1111-11-11', 'superadmin', '186cf774c97b60a1c106ef718d10970a6a06e06bef89553d9ae65d938a886eae', '2023-03-14 08:50:31', 'e5286fda', '1', NULL, '0', NULL);
INSERT INTO `user` (`id`, `role_id`, `first_name`, `last_name`, `phone`, `birth`, `email`, `password`, `registered_at`, `activation_code`, `is_activated`, `activated_at`, `is_deleted`, `deleted_at`)
VALUES (2, 2, 'admin', 'admin', 'admin', '1111-11-11', 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '2023-03-14 08:50:31', '8bd77ass', '1', NULL, '0', NULL);
INSERT INTO `user` (`id`, `role_id`, `first_name`, `last_name`, `phone`, `birth`, `email`, `password`, `registered_at`, `activation_code`, `is_activated`, `activated_at`, `is_deleted`, `deleted_at`)
VALUES (3, 3, 'student', 'student', 'student', '1111-11-11', 'student', '264c8c381bf16c982a4e59b0dd4c6f7808c51a05f64c35db42cc78a2a72875bb', '2023-03-14 10:24:21', '00a794f4', '1', NULL, '0', NULL);


INSERT INTO `user` (`id`, `role_id`, `first_name`, `last_name`, `phone`, `birth`, `email`, `password`, `registered_at`, `activation_code`, `is_activated`, `activated_at`, `is_deleted`, `deleted_at`)
VALUES (4, 2, 'Kiss', 'Pista', '003612345678', '1111-11-11', 'kisspista', 'asdfgh', '2023-03-14 10:24:21', 'q', '1', NULL, '0', NULL);
INSERT INTO `user` (`id`, `role_id`, `first_name`, `last_name`, `phone`, `birth`, `email`, `password`, `registered_at`, `activation_code`, `is_activated`, `activated_at`, `is_deleted`, `deleted_at`)
VALUES (5, 3, 'Kovacs', 'Tamas', '003612345678', '1111-11-11', 'kovacstamas', 'asdfgh', '2023-03-14 10:24:21', 'w', '1', NULL, '0', NULL);
INSERT INTO `user` (`id`, `role_id`, `first_name`, `last_name`, `phone`, `birth`, `email`, `password`, `registered_at`, `activation_code`, `is_activated`, `activated_at`, `is_deleted`, `deleted_at`)
VALUES (6, 3, 'Nagy', 'Bela', '003612345678', '1111-11-11', 'nagybela', 'asdfgh', '2023-03-14 10:24:21', 'e', '1', NULL, '0', NULL);
INSERT INTO `user` (`id`, `role_id`, `first_name`, `last_name`, `phone`, `birth`, `email`, `password`, `registered_at`, `activation_code`, `is_activated`, `activated_at`, `is_deleted`, `deleted_at`)
VALUES (7, 3, 'Voros', 'Mate', '003612345678', '1111-11-11', 'vorosmate', 'asdfgh', '2023-03-14 10:24:21', 'r', '1', NULL, '0', NULL);

INSERT INTO `user` (`id`, `role_id`, `first_name`, `last_name`, `phone`, `birth`, `email`, `password`, `registered_at`, `activation_code`, `is_activated`, `activated_at`, `is_deleted`, `deleted_at`)
VALUES (8, 2, 'Eros', 'Pista', '003612345678', '1111-11-11', 'erospista', 'asdfgh', '2023-03-14 10:24:21', 't', '1', NULL, '0', NULL);
INSERT INTO `user` (`id`, `role_id`, `first_name`, `last_name`, `phone`, `birth`, `email`, `password`, `registered_at`, `activation_code`, `is_activated`, `activated_at`, `is_deleted`, `deleted_at`)
VALUES (9, 3, 'Nagy', 'Sandor', '003612345678', '1111-11-11', 'nagysandor', 'asdfgh', '2023-03-14 10:24:21', 'z', '1', NULL, '0', NULL);
INSERT INTO `user` (`id`, `role_id`, `first_name`, `last_name`, `phone`, `birth`, `email`, `password`, `registered_at`, `activation_code`, `is_activated`, `activated_at`, `is_deleted`, `deleted_at`)
VALUES (10, 3, 'Kis', 'Alfonz', '003612345678', '1111-11-11', 'kisalfonz', 'asdfgh', '2023-03-14 10:24:21', 'u', '1', NULL, '0', NULL);
INSERT INTO `user` (`id`, `role_id`, `first_name`, `last_name`, `phone`, `birth`, `email`, `password`, `registered_at`, `activation_code`, `is_activated`, `activated_at`, `is_deleted`, `deleted_at`)
VALUES (11, 3, 'Horvath', 'Jozsef', '003612345678', '1111-11-11', 'horvathjozsef', 'asdfgh', '2023-03-14 10:24:21', 'i', '1', NULL, '0', NULL);


INSERT INTO `card` (`id`, `hash`,`created_at`,`is_deleted`,`deleted_at`,`is_assigned`,`last_assigned_to`)
VALUES (1, '01 01 01 01', '1111-11-11 08:08:08', 0, null, 1, 1);
INSERT INTO `card` (`id`, `hash`,`created_at`,`is_deleted`,`deleted_at`,`is_assigned`,`last_assigned_to`)
VALUES (2, '02 02 02 02', '1111-11-11 08:08:08', 0, null, 1, 5);
INSERT INTO `card` (`id`, `hash`,`created_at`,`is_deleted`,`deleted_at`,`is_assigned`,`last_assigned_to`)
VALUES (3, '03 03 03 03', '1111-11-11 08:08:08', 0, null, 0, null);

INSERT INTO `card` (`id`, `hash`,`created_at`,`is_deleted`,`deleted_at`,`is_assigned`,`last_assigned_to`)
VALUES (4, '04 04 04 04', '1111-11-11 08:08:08', 0, null, 0, null);
INSERT INTO `card` (`id`, `hash`,`created_at`,`is_deleted`,`deleted_at`,`is_assigned`,`last_assigned_to`)
VALUES (5, '05 05 05 05', '1111-11-11 08:08:08', 0, null, 0, null);
INSERT INTO `card` (`id`, `hash`,`created_at`,`is_deleted`,`deleted_at`,`is_assigned`,`last_assigned_to`)
VALUES (6, '06 06 06 06', '1111-11-11 08:08:08', 0, null, 0, null);

INSERT INTO `student` (`id`, `card_id`, `user_id`) VALUES (1, 1, 5);
INSERT INTO `student` (`id`, `card_id`, `user_id`) VALUES (2, null, 6);
INSERT INTO `student` (`id`, `card_id`, `user_id`) VALUES (3, null, 7);

INSERT INTO `student` (`id`, `card_id`, `user_id`) VALUES (4, null, 9);
INSERT INTO `student` (`id`, `card_id`, `user_id`) VALUES (5, 2, 10);
INSERT INTO `student` (`id`, `card_id`, `user_id`) VALUES (6, null, 11);

INSERT INTO `workgroup` (`id`, `group_name`, `institution`)
VALUES (1, 'Workgroup1_name', 'Institution_name');

INSERT INTO `workgroup` (`id`, `group_name`, `institution`)
VALUES (2, 'Workgroup2_name', 'Institution2_name');

INSERT INTO `workgroup_members` (`id`, `user_id`, `workgroup_id`) VALUES (1, 4, 1);
INSERT INTO `workgroup_members` (`id`, `user_id`, `workgroup_id`) VALUES (2, 5, 1);
INSERT INTO `workgroup_members` (`id`, `user_id`, `workgroup_id`) VALUES (3, 6, 1);
INSERT INTO `workgroup_members` (`id`, `user_id`, `workgroup_id`) VALUES (4, 7, 1);

INSERT INTO `workgroup_members` (`id`, `user_id`, `workgroup_id`) VALUES (5, 8, 2);
INSERT INTO `workgroup_members` (`id`, `user_id`, `workgroup_id`) VALUES (6, 9, 2);
INSERT INTO `workgroup_members` (`id`, `user_id`, `workgroup_id`) VALUES (7, 10, 2);
INSERT INTO `workgroup_members` (`id`, `user_id`, `workgroup_id`) VALUES (8, 11, 2);