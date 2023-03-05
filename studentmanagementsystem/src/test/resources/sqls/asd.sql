CREATE TABLE `role` (
  id INT AUTO_INCREMENT NOT NULL,
   role_type VARCHAR(255) NULL,
   CONSTRAINT pk_role PRIMARY KEY (id)
);
CREATE TABLE user (
  id INT AUTO_INCREMENT NOT NULL,
   first_name VARCHAR(255) NULL,
   last_name VARCHAR(255) NULL,
   phone VARCHAR(255) NULL,
   birth date NULL,
   email VARCHAR(255) NULL,
   password VARCHAR(255) NULL,
   registered_at datetime NULL,
   activation_code LONGTEXT NULL,
   is_activated BIT(1) NULL,
   activated_at datetime NULL,
   is_deleted BIT(1) NULL,
   deleted_at datetime NULL,
   role_id INT NULL,
   CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE user ADD CONSTRAINT FK_USER_ON_ROLE FOREIGN KEY (role_id) REFERENCES `role` (id);

insert into user (id,role_id) values (1,1);