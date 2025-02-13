package com.jr.grdb_backend.controlller;

import com.jr.grdb_backend.dto.UserDto;
import com.jr.grdb_backend.model.User;
import com.jr.grdb_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    private List<User> getAll(){
        return userService.getAll();
    }

    @PostMapping("/")
    private ResponseEntity addUser(UserDto userDto){

        User user = userService.addUser(userDto);

        if (user == null ){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A user with that email already exists");
        }
       return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
