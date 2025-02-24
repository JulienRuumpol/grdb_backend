package com.jr.grdb_backend.service;


import com.jr.grdb_backend.dto.GameDto;
import com.jr.grdb_backend.model.Game;

import java.util.List;

public interface GameService {

    List<Game> getAll();
    Game findById(Long id);
    Game updateGame(GameDto gameDto);
    Game createGame(GameDto gameDto);
    void deleteGame(Long id);
}
