ALTER  TABLE users
add role_id int;



ALTER TABLE users
 ADD FOREIGN KEY (role_id) REFERENCES role(id);
