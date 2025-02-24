package com.jr.grdb_backend.controller;

import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.model.Game;
import com.jr.grdb_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public List<CustomUser> getAll(){
        return userService.getAll();
    }

    @GetMapping("/{id}/game")
    public ResponseEntity<List<Game>> getGamebyUserId(@PathVariable Long id){
        return ResponseEntity.ok().body(userService.getGamesByUserId(id));
    }

    @GetMapping("/{id}/game/unique")
        public ResponseEntity<List<Game>> getGamesNotInUserList(@PathVariable Long id){
        return ResponseEntity.ok().body(userService.getGamesNotInUserList(id));
    }

    @PostMapping("/{userId}/game/{gameId}")
    public ResponseEntity<List<Game>> addGame(@PathVariable Long userId, @PathVariable Long gameId){
        return ResponseEntity.ok().body(userService.addGame(userId,gameId));

    }
}
