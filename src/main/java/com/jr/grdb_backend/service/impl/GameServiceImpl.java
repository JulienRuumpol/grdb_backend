package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.dto.GameDto;
import com.jr.grdb_backend.model.Game;
import com.jr.grdb_backend.repository.GameRepository;
import com.jr.grdb_backend.service.GameService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    private GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public List<Game> getAll() {
        return gameRepository.findAll();
    }

    @Override
    public Game findById(Long id) {
        return gameRepository.findById(id).orElseThrow(() -> new RuntimeException("Game with id: "+ id +" not found"));
    }

    @Override
    public Game updateGame(GameDto gameDto) {
        //todo find out if this is accurate
        Game oldGame = gameRepository.getReferenceById(gameDto.getId());
        Game newGame = this.gameDtoToGame(gameDto);


        //current fix because this returns a null object
         gameRepository.save(newGame);
         return newGame;
    }

    @Override
    public Game createGame(GameDto gameDto) {
        Game newGame = gameDtoToGame(gameDto);
        return gameRepository.save(newGame);
    }

    @Override
    public void deleteGame(Long id) {
        Game oldGame = gameRepository.getReferenceById(id);
        if (oldGame == null) {
            throw new RuntimeException("Game with id: " + id + "  not found ");
        }
        gameRepository.delete(oldGame);
    }

    private Game gameDtoToGame(GameDto gameDto) {
        return Game.builder()
                .id(gameDto.getId())
                .name(gameDto.getName())
                .description(gameDto.getDescription())
                .build();
    }
}
