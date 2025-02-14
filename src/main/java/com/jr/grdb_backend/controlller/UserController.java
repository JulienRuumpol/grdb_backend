package com.jr.grdb_backend.controlller;

import com.jr.grdb_backend.dto.UserDto;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/register")
    public ResponseEntity addUser(UserDto userDto){

        CustomUser user = userService.addUser(userDto);

        if (user == null ){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A user with that email already exists");
        }
       return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
