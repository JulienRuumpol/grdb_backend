package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.dto.UserDto;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.repository.UserRepository;
import com.jr.grdb_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


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
    public CustomUser addUser(UserDto dto) {
        CustomUser newUser = this.dtoToEntity(dto);
        if (userRepository.findByEmail(newUser.getEmail()).isEmpty()) {
            //todo throw error
            return null;
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.save(this.dtoToEntity(dto));
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

}
