update users
set is_account_non_expired = false;

update users
set  is_account_non_locked = true;

update users
set is_credential_non_expired = true;
