INSERT INTO Role (id,role_type) VALUES (1,'superadmin');
INSERT INTO Role (id,role_type) VALUES (2,'admin');
INSERT INTO Role (id,role_type) VALUES (3,'student');



INSERT INTO User (id,first_name, last_name, phone, birth, email, password, registered_at, activation_code, is_activated, activated_at, is_deleted, deleted_at, role_id)
VALUES (1, 'testFirstname', 'testLastname', 'testPhone', '1111-11-11', 'testEmail', 'e4e8a6e0f94409408b8bcce5d7757e5b648cbcab13b3570db1d0e4b95c79094c', NOW(), '1code', 1, NOW(), 0, NOW(), 1); --pw:testPw

INSERT INTO User (id,first_name, last_name, phone, birth, email, password, registered_at, activation_code, is_activated, activated_at, is_deleted, deleted_at, role_id)
VALUES (2, 'testFirstname2', 'testLastname2', 'testPhone2', '1112-11-11', 'testEmail2', 'd185d7e21e73a48437d56d140265177d89734edd98413896bf71ce72e021a872', NOW(), '2code', 1, NOW(), 0, NOW(), 1); --pw:testPw2