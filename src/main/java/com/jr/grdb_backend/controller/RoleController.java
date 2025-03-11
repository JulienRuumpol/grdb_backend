package com.jr.grdb_backend.controller;

import com.jr.grdb_backend.model.Role;
import com.jr.grdb_backend.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    private RoleService roleservice;

    @GetMapping("/")
    private ResponseEntity<List<Role>> getAll(){
        return ResponseEntity.ok().body(roleservice.getAll());
    }

}
