CREATE TABLE review
(
    id      BIGINT(255) PRIMARY KEY AUTO_INCREMENT,
    game_id BIGINT(255) NOT NULL,
    user_id BIGINT(255) NOT NULL,
    posted_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (game_id) REFERENCES game (id),
    UNIQUE (user_id, game_id)
)