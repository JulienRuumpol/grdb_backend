package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    @Mock
    private UserRepository userRepository;

    private CustomUser user;

    @BeforeEach
    void setUp() {
        this.user = CustomUser.builder()
                .id(1L)
                .email("test@test.com")
                .build();
    }

    /**
     * Unit test {@link CustomUserDetailService#loadUserByUsername(String)}
     * Scenario: Successfully retrieve a user through email
     */
    @Test
    void loadUserByUsername() {
        when(this.userRepository.findByEmail(this.user.getEmail())).thenReturn(Optional.ofNullable(this.user));

        CustomUser user = this.customUserDetailService.loadUserByUsername(this.user.getEmail());;
        verify(this.userRepository, times(1)).findByEmail(this.user.getEmail());

        assertEquals(user, this.user);


    }
    /**
     * Unit test {@link CustomUserDetailService#loadUserByUsername(String)}
     * Scenario: Unable to retrieve user through email because  user with that email doesn't exist
     */
    @Test
    void loadUserByUsernameUserNotFound() {
        String email = "nomail@mail.com";

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            this.customUserDetailService.loadUserByUsername(email);
        });

        assertEquals(exception.getMessage(), "User with email " +  email + " not found");
    }
}