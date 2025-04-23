package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.model.Game;
import com.jr.grdb_backend.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @InjectMocks
    private GameServiceImpl gameServiceImpl;
    @Mock
    private GameRepository gameRepository;

    private Game game;


    @BeforeEach
    void setUp() {
        this.game = Game.builder()
                .description("A game used for testing purpose")
                .id(1l)
                .name("Testing: The video game")
                .build();
    }

    /**
     * Unit test {@link GameServiceImpl#getAll()}
     * Scenario: Successfully retrieve all games
     */
    @Test
    void getAll() {

    }

    @Test
    void findById() {
    }

    @Test
    void updateGame() {
    }

    @Test
    void createGame() {
    }

    @Test
    void deleteGame() {
    }
}