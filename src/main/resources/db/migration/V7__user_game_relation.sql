create table user_game(
    user_id BIGINT(255),
    game_id BIGINT(255),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (game_id) REFERENCES game(id)
)
