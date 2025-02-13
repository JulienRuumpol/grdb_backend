package com.jr.grdb_backend.service;

import com.jr.grdb_backend.dto.UserDto;
import com.jr.grdb_backend.model.User;

import java.util.List;

public interface UserService {

   List<User> getAll();

   User addUser(UserDto dto);
}
