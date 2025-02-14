package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.dto.UserDto;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.encoder= new BCryptPasswordEncoder(16);
    }

//    @Override
//    public List<User> getAll() {
//        return userRepository.findAll();
//    }
//
//    @Override
//    public User addUser(UserDto dto) {
//        User newUser = this.dtoToEntity(dto);
//        if (userRepository.findByEmail(newUser.getEmail()) != null) {
//            return null;
//        }
//        newUser.setPassword(encoder.encode(newUser.getPassword()));
//
//        return userRepository.save(this.dtoToEntity(dto));
//    }

//    private User dtoToEntity(UserDto dto) {
//        return User.builder()
//                .email(dto.getEmail())
//                .password(dto.getPassword())
//                .userName(dto.getUserName())
//                .firstName(dto.getFirstName())
//                .lastName(dto.getLastName())
//                .language(dto.getLanguage())
//                .build();
//    }
//
//    private UserDto UserToDto(User user) {
//        return UserDto.builder()
//                .email(user.getEmail())
//                .password(user.getPassword())
//                .userName(user.getUsername())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .language(user.getLanguage())
//                .build();
//    }
//
//    private User dtoToNewUser(UserDto dto){
//        return User.builder()
//                .email(dto.getEmail())
//                .password(dto.getPassword())
//                .userName(dto.getUserName())
//                .firstName(dto.getFirstName())
//                .lastName(dto.getLastName())
//                .language(dto.getLanguage())
//                .isEnabled(true)
//                .isAccountNonLocked(true)
//                .isCredentialsNonExpired(true)
//                .isAccountNonExpired(true)
//                .build();
//    }
//
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // rewriting this to use email

        Optional<CustomUser> user = userRepository.findByEmail(username);
        if (!user.isPresent()) {
            //todo check if this code is valid might be missing initializing object?
            var userObj = user.get();

            return User.builder()
                    .password(userObj.getPassword())
                    .username(userObj.getUserName())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
