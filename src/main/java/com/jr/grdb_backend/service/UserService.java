package com.jr.grdb_backend.service;

import com.jr.grdb_backend.dto.*;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.model.Game;
import com.jr.grdb_backend.model.Role;

import java.util.List;

public interface UserService {

   List<UserDto> getAll();

   CustomUser findByEmail(String email);
   CustomUser registerDtoToUser(RegisterDto dto);
   List<Game> getGamesByUserId(Long userId);
   List<Game> addGame(Long userId, Long gameId);
   List<Game> getGamesNotInUserList(Long userId);
   CustomUser updateLanguage(Long userId, LanguageDto newLanguage);
   UserDto getCustomUserById(Long userId);
   String getCustomUserThroughAuthentication();
   UserDto getUserByEmail(String email);
   UserDto updateRole(Long userId,  Role role);
   Boolean updateUserPassword(Long userId, ChangePasswordDto changePasswordDto);
   UserDto updateUserDetails(Long userId, ChangeUserDetailsDto changeUserDetailsDto);
}

