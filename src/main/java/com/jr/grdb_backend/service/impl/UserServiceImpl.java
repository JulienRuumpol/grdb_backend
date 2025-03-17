package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.dto.ChangePasswordDto;
import com.jr.grdb_backend.dto.LanguageDto;
import com.jr.grdb_backend.dto.RegisterDto;
import com.jr.grdb_backend.dto.UserDto;
import com.jr.grdb_backend.enume.Language;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.model.Game;
import com.jr.grdb_backend.model.Role;
import com.jr.grdb_backend.repository.RoleRepository;
import com.jr.grdb_backend.repository.UserRepository;
import com.jr.grdb_backend.service.GameService;
import com.jr.grdb_backend.service.RoleService;
import com.jr.grdb_backend.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private GameService gameService;
    private RoleService roleService;
    @Autowired
    private RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository,
                           GameService gameService,
                           RoleService roleService) {
        this.userRepository = userRepository;
        this.gameService = gameService;
        this.roleService = roleService;
    }

    @Override
    public List<CustomUser> getAll() {
        return userRepository.findAll();
    }

    @Override
    public CustomUser findByEmail(String email) {
        Optional<CustomUser> user = userRepository.findByEmail(email);

        return user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public List<Game> getGamesByUserId(Long userId) {
        return userRepository.findGamesByUserId(userId);
    }

    @Override
    public List<Game> addGame(Long userId, Long gameId) {
        CustomUser user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Game game = gameService.findById(gameId);

        if (user.getGames().contains(game)) throw new RuntimeException("A user can not contain a duplicate game");

        user.addGameToGames(game);

        return userRepository.save(user).getGames();
    }

    @Override
    public List<Game> getGamesNotInUserList(Long userId) {
        CustomUser user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Game> games = user.getGames();

        List<Game> allGamesList = gameService.getAll();

        return allGamesList.stream().filter(word -> !new HashSet<>(games).contains(word)).toList();
    }

    @Override
    public CustomUser updateLanguage(Long userId, LanguageDto languageDto) {
        CustomUser user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setLanguage(languageDto.getLanguage());
        return userRepository.save(user);
    }

    @Override
    public UserDto getCustomUserById(Long userId) {
        Optional<CustomUser> user = userRepository.findById(userId);
        if (user.isPresent()) return userToDto(user.get());
        else throw new RuntimeException("User not found");
    }

    @Override
    @Transactional
    public String getCustomUserThroughAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUser user = (CustomUser) authentication.getPrincipal();
        return user.getUsername();
    }

    @Override
    public UserDto getUserByEmail(String email) {
        Optional<CustomUser> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return userToDto(user.get());
        }
        throw new RuntimeException("User not found");
    }

    @Override
    public UserDto updateRole(Long userId, Role role) {

        CustomUser user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Role newRole = roleRepository.findById(role.getId()).orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(newRole);

        userRepository.save(user);

        UserDto dtoUser = userToDto(user);

        return dtoUser;
    }

    @Override
    public Boolean updateUserPassword(Long userId, ChangePasswordDto changePasswordDto) {

        CustomUser user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);

        return true;
    }


    private CustomUser dtoToEntity(UserDto dto) {
        return CustomUser.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .userName(dto.getUserName())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .language(dto.getLanguage())
                .role(dto.getRole())
                .build();
    }

    private UserDto userToDto(CustomUser user) {
        return UserDto.builder()
                .email(user.getEmail())
//                .password(user.getPassword())
                .password("")
                .userName(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .language(user.getLanguage())
                .role(user.getRole())
                .build();
    }

    public CustomUser registerDtoToUser(RegisterDto dto) {
        return CustomUser.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .userName(dto.getUserName())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .enabled(true)
                .isCredentialNonExpired(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .language(Language.DUTCH)
                .build();
    }

}

