insert into role (name) values ('ROLE_USER');
insert into role (name) values ('ROLE_ADMIN');

insert into users (email, password, role_id, is_enabled, is_deleted) values ('admin@gmail.com', '$2a$10$t5B4Vu20.u//zjDP2IU4kOR49tqzbUo9WRVQ50rV1Og3FxBsioG2C', 2, true, false);

insert into certificate(id, serial_number, alias, valid_from, valid_to, user_id) values (nextval('certificate_id_seq'), '5398475839465654', 'reddit', '2021-03-01 12:30:00', '2021-05-01 12:30:00', 1);
insert into certificate(id, serial_number, alias, valid_from, valid_to, user_id) values (nextval('certificate_id_seq'), '759308475935893', 'github', '2021-03-01 12:30:00', '2025-05-01 12:30:00', 1);
