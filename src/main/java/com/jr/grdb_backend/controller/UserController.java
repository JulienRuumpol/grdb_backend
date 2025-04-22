package com.jr.grdb_backend.controller;

import com.jr.grdb_backend.controller.exceptions.AlreadyExistingEmailException;
import com.jr.grdb_backend.controller.responses.ChangePasswordResponse;
import com.jr.grdb_backend.dto.ChangePasswordDto;
import com.jr.grdb_backend.dto.ChangeUserDetailsDto;
import com.jr.grdb_backend.dto.LanguageDto;
import com.jr.grdb_backend.dto.UserDto;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.model.Game;
import com.jr.grdb_backend.model.Role;
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
    public List<CustomUser> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}/game")
    public ResponseEntity<List<Game>> getGamebyUserId(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getGamesByUserId(id));
    }

    @GetMapping("/{id}/game/unique")
    public ResponseEntity<List<Game>> getGamesNotInUserList(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getGamesNotInUserList(id));
    }

    @PostMapping("/{userId}/game/{gameId}")
    public ResponseEntity<List<Game>> addGame(@PathVariable Long userId, @PathVariable Long gameId) {
        return ResponseEntity.ok().body(userService.addGame(userId, gameId));

    }

    @PutMapping("/{userId}/language")
    public ResponseEntity<CustomUser> setLanguage(@PathVariable Long userId, @RequestBody LanguageDto language) {
        return ResponseEntity.ok().body(this.userService.updateLanguage(userId, language));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {

        UserDto userDto = this.userService.getCustomUserById(userId);

        return ResponseEntity.ok().body(this.userService.getCustomUserById(userId));
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<UserDto> updateRole(@PathVariable Long userId, @RequestBody Role role) {

        return ResponseEntity.ok().body(this.userService.updateRole(userId, role));
    }

    @PutMapping("{userId}/password")
    public ResponseEntity<ChangePasswordResponse> updateUserPassword(@PathVariable long userId, @RequestBody ChangePasswordDto changePassword){

        boolean changeSuccess = this.userService.updateUserPassword(userId, changePassword);

        if (changeSuccess){
            ChangePasswordResponse respose = new ChangePasswordResponse(200,"password changed");
            return ResponseEntity.ok().body(respose);
        } else {
            ChangePasswordResponse respose = new ChangePasswordResponse(404,"Old password incorrect or unable to find user");
            return ResponseEntity.status(404).body(respose);
        }
    }

    @PutMapping("{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody ChangeUserDetailsDto userDto) {

        try{
            return ResponseEntity.ok().body(userService.updateUserDetails(userId, userDto));
        }catch (AlreadyExistingEmailException e){
            return ResponseEntity.status(409).body(new UserDto());
        }
    }

//    @GetMapping("/{email}")
//    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
//        return ResponseEntity.ok().body(this.userService.getUserByEmail(email));
//    }
}
