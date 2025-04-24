package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.dto.GameDto;
import com.jr.grdb_backend.model.Game;
import com.jr.grdb_backend.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @InjectMocks
    private GameServiceImpl gameService;
    @Mock
    private GameRepository gameRepository;

    private Game game;
    private List<Game> gamesList = new ArrayList<>();


    @BeforeEach
    void setUp() {
        this.game = Game.builder()
                .description("A game used for testing purpose")
                .id(1l)
                .name("Testing: The video game")
                .build();
        this.gamesList.add(this.game);
    }

    /**
     * Unit test {@link GameServiceImpl#getAll()}
     * Scenario: Successfully retrieve all games
     */
    @Test
    void getAll() {
        when(this.gameRepository.findAll()).thenReturn(this.gamesList);

        List<Game> games = this.gameService.getAll();

        verify(this.gameRepository, times(1)).findAll();

        assertEquals(1, games.size());

    }

    /**
     * Unit test {@link GameServiceImpl#findById(Long)}
     * Scenario: Successfully retrieve a game by id
     */
    @Test
    void findById() {
        when(this.gameRepository.findById(this.game.getId())).thenReturn(Optional.ofNullable(this.game));

        Game game = this.gameService.findById(this.game.getId());

        verify(this.gameRepository, times(1)).findById(this.game.getId());
        assertEquals(this.game.getId(), game.getId());
    }

    /**
     * Unit test {@link GameServiceImpl#findById(Long)}
     * Scenario: fail to retrieve a game due to game with said id not existing.
     */
    @Test
    void unableToFindByIdea() {


        Exception exception = assertThrows(RuntimeException.class, () -> {
            this.gameService.findById(this.game.getId());
        });

        assertEquals(exception.getMessage(), "Game with id: " + this.game.getId() +" not found");

    }

    /**
     * Unit test {@link GameServiceImpl#updateGame(GameDto)}
     * Scenario: successfully update a game's information.
     */
    @Test
    void updateGame() {
        String newDescription = "Updated game description";
        String newName = "Testing 2";

        GameDto gameDto = new GameDto();
        gameDto.setId(this.game.getId());
        gameDto.setDescription(newDescription);
        gameDto.setName(newName);

        when(this.gameRepository.getReferenceById(this.game.getId())).thenReturn(this.game);
        when(this.gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Game newGame = this.gameService.updateGame(gameDto);
        assertEquals(newGame.getDescription(), newDescription);
        assertEquals(newGame.getName(), newName);



    }

    /**
     * Unit test {@link GameServiceImpl#createGame(GameDto)}
     * Scenario: successfully create a new game.
     */

    @Test
    void createGame() {
        String newDescription = "Updated game description";
        String newName = "Testing 2";

        GameDto gameDto = new GameDto();
        gameDto.setDescription(newDescription);
        gameDto.setName(newName);

        when(this.gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));


        Game newGame = this.gameService.createGame(gameDto);
        assertEquals(newGame.getDescription(), newDescription);
        assertEquals(newGame.getName(), newName);

    }
    /**
     * Unit test {@link GameServiceImpl#deleteGame(Long)}
     * Scenario: successfully delete a game
     */
    @Test
    void deleteGame() {

        when(this.gameRepository.getReferenceById(this.game.getId())).thenReturn((this.game));

        this.gameService.deleteGame(this.game.getId());
        verify(this.gameRepository, times(1)).getReferenceById(this.game.getId());
        verify(this.gameRepository, times(1)).delete(this.game);

    }
    /**
     * Unit test {@link GameServiceImpl#deleteGame(Long)}
     * Scenario: Unable to delete game because a game with given id doesn't exist
     */
    @Test
    void deleteGameButIdNotFound() {

        when(this.gameRepository.getReferenceById(this.game.getId())).thenReturn((this.game));

        this.gameService.deleteGame(this.game.getId());
        verify(this.gameRepository, times(1)).getReferenceById(this.game.getId());
        verify(this.gameRepository, times(1)).delete(this.game);

    }
}