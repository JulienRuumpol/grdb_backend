ALTER TABLE users
DROP PRIMARY KEY;

alter table users
    ADD id BIGINT(255) PRIMARY KEY AUTO_INCREMENT,
    ADD is_account_non_expired Boolean,
    ADD is_account_non_locked Boolean,
    ADD is_credential_non_expired Boolean,
    ADD is_enabled Boolean;
