package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.dto.RegisterDto;
import com.jr.grdb_backend.dto.UserDto;
import com.jr.grdb_backend.enume.Language;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.model.Game;
import com.jr.grdb_backend.repository.UserRepository;
import com.jr.grdb_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    private CustomUser dtoToEntity(UserDto dto) {
        return CustomUser.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .userName(dto.getUserName())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .language(dto.getLanguage())
                .build();
    }

    private UserDto UserToDto(CustomUser user) {
        return UserDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .userName(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .language(user.getLanguage())
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

