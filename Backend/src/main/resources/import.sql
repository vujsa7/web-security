insert into role (name) values ('ROLE_USER');
insert into role (name) values ('ROLE_ADMIN');
insert into users (first_name, last_name, email, password, phone_number, role_id, is_enabled, is_deleted) values ('Admin', 'Admin', 'admin@gmail.com', '$2a$10$t5B4Vu20.u//zjDP2IU4kOR49tqzbUo9WRVQ50rV1Og3FxBsioG2C', '12345', 1, true, false);