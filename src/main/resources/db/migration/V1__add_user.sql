create table users(
    id BIGINT(255) PRIMARY KEY AUTO_INCREMENT,
    username varchar(255),
    password varchar(255) NOT NULL,
    firstname varchar(255),
    lastname varchar(255),
    email varchar(255) NOT NULL,
    language varchar(255)
)
