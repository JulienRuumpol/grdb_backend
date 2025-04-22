package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.controller.exceptions.AlreadyExistingEmailException;
import com.jr.grdb_backend.dto.*;
import com.jr.grdb_backend.enume.Language;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.model.Game;
import com.jr.grdb_backend.model.Role;
import com.jr.grdb_backend.repository.GameRepository;
import com.jr.grdb_backend.repository.RoleRepository;
import com.jr.grdb_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests {@link UserServiceImpl}
 */

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GameServiceImpl gameService;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleServiceImpl roleServiceImpl;
    @Mock
    private GameRepository gameRepository;

    @Mock
    private SecurityContextHolder securityContextHolder;
    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CustomUser user;
    private Role role;
    private Game game;

    @BeforeEach
    void setUp() {
        this.role = new Role();
        this.role.setName("Admin");
        this.role.setId(1l);

        this.game = new Game();
        this.game.setId(1l);
        this.game.setDescription("a new testing game");
        this.game.setName("testing the game");

        this.user = new CustomUser();
        this.user.setId(1l);
        this.user.setUserName("Tester");
        this.user.setRole(this.role);
        this.user.setEmail("test@test.com");
        this.user.setPassword("test");
        this.user.setFirstName("Tester");
        this.user.setLastName("Tested");
        this.user.addGameToGames(game);
    }

    /**
     * Unit test {@link UserServiceImpl#getAll()}
     * Scenario: Successfully get all users
     */
    @Test
    void getAll() {
        when(this.userRepository.findAll()).thenReturn(Collections.singletonList(this.user));

        List<CustomUser> users = this.userService.getAll();

        verify(this.userRepository, times(1)).findAll();
        assertEquals(1, users.size());

    }

    /**
     * Unit test {@link UserServiceImpl#findByEmail(String)}
     * Scenario: Successfully find a user by email
     */
    @Test
    void findByEmail() {
        when(this.userRepository.findByEmail(this.user.getEmail())).thenReturn(Optional.ofNullable(this.user));

        CustomUser user = this.userService.findByEmail(this.user.getEmail());
        verify(this.userRepository, times(1)).findByEmail(this.user.getEmail());
        assertEquals(this.user.getEmail(), user.getEmail());

    }

    /**
     * Unit test {@link UserServiceImpl#findByEmail(String)}
     * Scenario: Successfully find a user by email
     */
    @Test
    void UserNotFoundByEmail() {
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            this.userService.findByEmail("fake@test.com");
        });

        assertEquals("User not found", exception.getMessage());

    }

    /**
     * Unit test {@link UserServiceImpl#getGamesByUserId(Long)}
     * Scenario: Successfully get all games for a user by user id
     */
    @Test
    void getGamesByUserId() {
        when(this.userRepository.findGamesByUserId(this.user.getId())).thenReturn(Collections.singletonList(this.game));
        List<Game> games = this.userService.getGamesByUserId(this.user.getId());
        verify(this.userRepository, times(1)).findGamesByUserId(this.user.getId());
        assertEquals(1, games.size());

    }

    /**
     * Unit test {@link UserServiceImpl#addGame(Long, Long)}
     * Scenario: Successfully add a game to a user
     */
    @Test
    void addGame() {
        Game newGame = Game.builder()
                .id(2l)
                .name("testing 2: eletric boogaloo")
                .description("a 2nd new game for testing")
                .build();

        when(this.userRepository.findById(this.user.getId())).thenReturn(Optional.ofNullable(this.user));
        when(this.gameService.findById(newGame.getId())).thenReturn(newGame);
        when(this.userRepository.save(any(CustomUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //might need to add when for gamerepo


        List<Game> games = this.userService.addGame(this.user.getId(), newGame.getId());

        assertEquals(2, games.size());

    }

    /**
     * Unit test {@link UserServiceImpl#addGame(Long, Long)}
     * Scenario: unable to add game due to adding an already existing game in the user's library
     */
    @Test
    void unableToAddGameDueToAlreadyInLibrary() {

        when(this.userRepository.findById(this.user.getId())).thenReturn(Optional.ofNullable(this.user));
        when(this.gameService.findById(this.game.getId())).thenReturn(this.game);
//        when(this.userRepository.save(any(CustomUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //might need to add when for gamerepo


        Exception exception = assertThrows(RuntimeException.class, () -> {
            this.userService.addGame(this.user.getId(), this.game.getId());
        });


        assertEquals("A user can not contain a duplicate game", exception.getMessage());
    }

    /**
     * Unit test {@link UserServiceImpl#getGamesNotInUserList(Long)}
     * Scenario: find games not already in user game list
     */
    @Test
    void getGamesNotInUserList() {
        List<Game> newGames = new ArrayList<>();
        for (int i = 2; i < 5; i++) {
            Game newGame = Game.builder()
                    .name("new game: " + i)
                    .description("a new game" + i)
                    .id((long) i)
                    .build();
            newGames.add(newGame);
        }

        when(this.userRepository.findById(this.user.getId())).thenReturn(Optional.ofNullable(this.user));
        when(this.gameService.getAll()).thenReturn(newGames);

        List<Game> gamesNotInUserList = this.userService.getGamesNotInUserList(this.user.getId());

        assertEquals(3, gamesNotInUserList.size());

    }

    /**
     * Unit test {@link UserServiceImpl#updateLanguage(Long, LanguageDto)}
     * Scenario: Successfully update the user's language
     */
    @Test
    void updateLanguage() {
        LanguageDto languageDto = new LanguageDto();
        languageDto.setLanguage(Language.DUTCH);

        when(this.userRepository.findById(this.user.getId())).thenReturn(Optional.ofNullable(this.user));
        when(this.userRepository.save(any(CustomUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomUser exampleUser = this.user;
        exampleUser.setLanguage(Language.DUTCH);

        CustomUser updatedUser = this.userService.updateLanguage(exampleUser.getId(), languageDto);

        verify(this.userRepository, times(1)).save(any(CustomUser.class));
        assertEquals(updatedUser.getLanguage(), exampleUser.getLanguage());
    }

    /**
     * Unit test {@link UserServiceImpl#getCustomUserById(Long)}
     * Scenario: Successfully retrieve a user by Id
     */
    @Test
    void getCustomUserById() {
        when(this.userRepository.findById(this.user.getId())).thenReturn(Optional.ofNullable(this.user));

        UserDto dtoExample = UserDto.builder()
                .email(this.user.getEmail())
                .role(this.user.getRole())
                .userName(this.user.getUsername())
                .language(this.user.getLanguage())
                .firstName(this.user.getFirstName())
                .lastName(this.user.getLastName())
//                .password(this.user.getPassword())
                .build();

        UserDto user = this.userService.getCustomUserById(this.user.getId());
        verify(this.userRepository, times(1)).findById(this.user.getId());

    }

    /**
     * Unit test {@link UserServiceImpl#getCustomUserThroughAuthentication()}
     * Scenario: Successfully retrieve a user through Authentication
     */
    @Test
    void getCustomUserThroughAuthentication() {
        when(this.securityContext.getAuthentication()).thenReturn(this.authentication);
        SecurityContextHolder.setContext(securityContext);

        when(this.authentication.getPrincipal()).thenReturn(this.user);

        String username = this.userService.getCustomUserThroughAuthentication();
        assertEquals(username, this.user.getUsername());


    }

    /**
     * Unit test {@link UserServiceImpl#getUserByEmail(String)}
     * Scenario: Throw a runtimeException because user isn't found
     */
    @Test
    void getUserByEmailNoUserFound() {
        when(this.userRepository.findByEmail(this.user.getEmail())).thenReturn(Optional.ofNullable(this.user));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            this.userService.findByEmail("fake@test.com");
        });

        //todo readd this
//        assertEquals(exception.getMessage(), "User not found");
    }

    /**
     * Unit test {@link UserServiceImpl#getUserByEmail(String)}
     * Scenario: Successfully retrieve a user through Email
     */
    @Test
    void getUserByEmail() {
        when(this.userRepository.findByEmail(this.user.getEmail())).thenReturn(Optional.ofNullable(this.user));

        UserDto retrievedUser = this.userService.getUserByEmail(this.user.getEmail());
        UserDto userDto = this.userService.userToDto(this.user);
        verify(this.userRepository, times(1)).findByEmail(this.user.getEmail());
        assertEquals(retrievedUser, userDto);
    }

    /**
     * Unit test {@link UserServiceImpl#updateRole(Long, Role)}
     * Scenario: Successfully update a user's role
     */
    @Test
    void updateRole() {
        Role newRole = Role.builder()
                .id(2l)
                .name("Basic")
                .build();
        when(this.userRepository.findById(this.user.getId())).thenReturn(Optional.ofNullable(this.user));
        when(this.roleRepository.findById(newRole.getId())).thenReturn(Optional.of(newRole));

        when(this.userRepository.save(any(CustomUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto oldUserDto = this.userService.userToDto(this.user);

        UserDto newUserDto = this.userService.updateRole(this.user.getId(), newRole);
        assertNotEquals(newUserDto, oldUserDto);
        assertEquals(newUserDto.getRole(),newRole);

    }

    /**
     * Unit test {@link UserServiceImpl#updateUserPassword(Long, ChangePasswordDto)}
     * Scenario: Successfully update a user's password
     */
    @Test
    void updateUserPassword() {
        String newPassword  = "newPassword";
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setNewPassword(newPassword);
        changePasswordDto.setOldPassword(this.user.getPassword());

        when(this.userRepository.findById(this.user.getId())).thenReturn(Optional.ofNullable(this.user));

        when(this.userRepository.save(any(CustomUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //todo remove this and make sure password matching works
        when(this.passwordEncoder.matches(changePasswordDto.getOldPassword(), this.user.getPassword())).thenReturn(true);

        boolean hasPasswordChanged = this.userService.updateUserPassword(this.user.getId(), changePasswordDto);
        assertTrue(hasPasswordChanged);


    }

    /**
     * Unit test {@link UserServiceImpl#updateUserDetails(Long, ChangeUserDetailsDto)} 
     * Scenario: Successfully update a user's details 
     * 
     */
    @Test
    void updateUserDetails() {
        ChangeUserDetailsDto changeUserDetailsDto = new ChangeUserDetailsDto();
        changeUserDetailsDto.setEmail(this.user.getEmail());
        changeUserDetailsDto.setUsername("Tom");
        changeUserDetailsDto.setFirstname("Jimmy");
        changeUserDetailsDto.setLastname("Jers");

        when(this.userRepository.findByEmail(changeUserDetailsDto.getEmail())).thenReturn(Optional.ofNullable(this.user));
        when(this.userRepository.findById(this.user.getId())).thenReturn(Optional.ofNullable(this.user));
        when(this.userRepository.save(any(CustomUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomUser assertUser = this.user;
        assertUser.setUserName(changeUserDetailsDto.getUsername());
        assertUser.setFirstName(changeUserDetailsDto.getFirstname());
        assertUser.setLastName(changeUserDetailsDto.getLastname());
        assertUser.setEmail(changeUserDetailsDto.getEmail());
        UserDto assertUserDto = this.userService.userToDto(assertUser);

        UserDto changedUser = this.userService.updateUserDetails(this.user.getId(), changeUserDetailsDto);

        assertEquals(changedUser, assertUserDto);

    }

    /**
     * Unit test {@link UserServiceImpl#updateUserDetails(Long, ChangeUserDetailsDto)}
     * Scenario: Unable to update user due to user exisiting with the same email
     */
    @Test
    void failToUpdateUserDetailsDueToSameEmail() {
        CustomUser sameEmailUser = new CustomUser();
        sameEmailUser.setEmail(this.user.getEmail());
        sameEmailUser.setId(4l);

        ChangeUserDetailsDto changeUserDetailsDto = new ChangeUserDetailsDto();
        changeUserDetailsDto.setEmail(this.user.getEmail());
        changeUserDetailsDto.setUsername("Tom");
        changeUserDetailsDto.setFirstname("Jimmy");
        changeUserDetailsDto.setLastname("Jers");

        when(this.userRepository.findByEmail(changeUserDetailsDto.getEmail())).thenReturn(Optional.of(sameEmailUser));
        when(this.userRepository.findById(this.user.getId())).thenReturn(Optional.ofNullable(this.user));

        Exception exception = assertThrows(AlreadyExistingEmailException.class, () -> {
           this.userService.updateUserDetails(this.user.getId(), changeUserDetailsDto);;
        });

        assertEquals(exception.getMessage(), "A user with " + changeUserDetailsDto.getEmail() +" already exists");

    }

    /**
     * Unit test {@link UserServiceImpl#userToDto(CustomUser)}
     * Scenario: Convert a user to a Dto succesfully
     */
    @Test
    void userToDto(){
        UserDto userDtoExample = UserDto.builder()
                .email(this.user.getEmail())
//                .password(user.getPassword())
                .password("")
                .userName(this.user.getUsername())
                .firstName(this.user.getFirstName())
                .lastName(this.user.getLastName())
                .language(this.user.getLanguage())
                .role(this.user.getRole())
                .build();

        UserDto userDtoConverted = this.userService.userToDto(this.user);

        assertEquals(userDtoConverted, userDtoExample);
    }

    /**
     * Unit test {@link UserServiceImpl#registerDtoToUser(RegisterDto)}
     * Scenario: Convert a RegisterDto to a User entity
     */

//    @Test
//    void registerDtoToUser() {
//        //todo implement test
//        RegisterDto registerDto = new RegisterDto();
//    }
}