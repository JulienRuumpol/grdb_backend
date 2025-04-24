package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.dto.RegisterDto;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserRepository userRepository;

    private CustomUser user;
    @Mock
    private UserServiceImpl userService;


    @BeforeEach
    void setUp() {
        this.user = new CustomUser();

        this.user.setId(1L);
        this.user.setUserName("testmachine");
        this.user.setEmail("testing@test.nl");
    }


    /**
     * Unit test {@link AuthenticationServiceImpl#register(RegisterDto)}
     * Scenario: Successfully register a user
     */
    @Test
    void register() {

        String newEmail = "email@mail.com";
        String newUsername = "testmachine";
        CustomUser customUser = new CustomUser();
        customUser.setId(2L);
        customUser.setUserName(newUsername);
        customUser.setEmail(newEmail);

        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("email@meail.com");
        registerDto.setUserName("newUsername");
        when(this.userRepository.save(any(CustomUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(this.userService.registerDtoToUser(registerDto)).thenReturn(customUser);
        CustomUser newUser = this.authenticationService.register(registerDto);

        assertEquals(newUser, customUser);


    }

    @Test
    void authenticate() {
    }

    @Test
    void refreshToken() {
    }
}