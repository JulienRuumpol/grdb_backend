package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.dto.UserDto;
import com.jr.grdb_backend.model.User;
import com.jr.grdb_backend.repository.UserRepository;
import com.jr.grdb_backend.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.encoder= new BCryptPasswordEncoder(16);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User addUser(UserDto dto) {
        User newUser = this.dtoToEntity(dto);
        if (userRepository.findByEmail(newUser.getEmail()) != null) {
            return null;
        }

        newUser.setPassword(encoder.encode(newUser.getPassword()));

        return userRepository.save(this.dtoToEntity(dto));
    }

    private User dtoToEntity(UserDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .userName(dto.getUserName())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .language(dto.getLanguage())
                .build();
    }

    private UserDto UserToDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .language(user.getLanguage())
                .build();
    }
}
