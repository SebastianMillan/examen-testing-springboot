INSERT INTO user_entity (id, username, password, avatar, full_name, account_non_expired, account_non_locked, credentials_non_expired, enabled, created_at, last_password_change_at) VALUES ('f15a6a84-828a-46fc-87ee-9735df4a7ca3', 'admin1', '1234', 'png', TRUE, TRUE, TRUE, TRUE, TRUE, NULL, NULL);
INSERT INTO user_entity (id, username, password, avatar, full_name, account_non_expired, account_non_locked, credentials_non_expired, enabled, created_at, last_password_change_at) VALUES ('f15a6a84-828a-46fc-87ee-9735df4a7ca2', 'user1', '1234', 'png', TRUE, TRUE, TRUE, TRUE, TRUE, NULL, NULL);

INSERT INTO user_roles (user_id, roles) VALUES ('f15a6a84-828a-46fc-87ee-9735df4a7ca3', 1);
INSERT INTO user_roles (user_id, roles) VALUES ('f15a6a84-828a-46fc-87ee-9735df4a7ca2', 2);

