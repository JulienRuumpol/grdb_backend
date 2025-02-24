package com.jr.grdb_backend.controller;

import com.jr.grdb_backend.dto.GameDto;
import com.jr.grdb_backend.model.Game;
import com.jr.grdb_backend.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Game>> getAll() {
        return ResponseEntity.ok().body(gameService.getAll());
    }

    //get by id
    @GetMapping("/{id}")
    public ResponseEntity<Game> getById(@PathVariable long id) {
        //todo add error handling when entity with ID is not found
        return ResponseEntity.ok().body(gameService.findById(id));
    }

    //update game
    @PutMapping("/")
    public ResponseEntity<Game> update(@RequestBody GameDto gameDto) {
        return ResponseEntity.ok().body(gameService.updateGame(gameDto));
    }


    //add game
    @PostMapping("/")
    public ResponseEntity<Game> create(@RequestBody GameDto gameDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.createGame(gameDto));
    }

    // delete game
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        gameService.deleteGame(id);
        return ResponseEntity.ok().body("Game with id " +id + " deleted");
    }
}
