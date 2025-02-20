package com.jr.grdb_backend.controller;

import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.model.Game;
import com.jr.grdb_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
