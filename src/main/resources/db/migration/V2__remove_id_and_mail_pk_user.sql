alter table users
DROP COLUMN id;

alter table users
ADD PRIMARY KEY (email);