package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.dto.ReviewDto;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.model.Game;
import com.jr.grdb_backend.model.Review;
import com.jr.grdb_backend.repository.GameRepository;
import com.jr.grdb_backend.repository.ReviewRepository;
import com.jr.grdb_backend.repository.UserRepository;
import com.jr.grdb_backend.service.ReviewService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class) // Important!

class ReviewServiceImplTest {



    @InjectMocks
    private ReviewService reviewService;
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private GameRepository gameRepository;
    @Mock
    private UserRepository userRepository;
    private Review review;
    private Game game;
    private CustomUser user;

    public ReviewServiceImplTest(ReviewRepository reviewRepository,
                                 GameRepository gameRepository,
                                 UserRepository userRepository
                                 ) {
        this.reviewRepository =reviewRepository;
        this.gameRepository = gameRepository;
        this.userRepository =  userRepository;
    }

    @BeforeEach
    void setUp() {


        this.game = new Game();
        game.setId(1l);
        game.setDescription("a new testing game");
        game.setName("testing the game");

        this.user = new CustomUser();
        user.setId(1l);
        user.addGameToGames(game);

        this.review = new Review();
        review.setId(1l);
        review.setDescription("testing");
        review.setPostedDate(new Date());
        review.setGame(game);
    }

    /**
     * Unit test {@link ReviewServiceImpl#getReviewsByGameId(Long)}
     * Scenario: Successfully retrieve reviews by ID
     */

    @Test
    void getReviewsByGameId() {
//        when(reviewRepository.findAllByGameId(game.getId())).thenReturn(Optional.of(review));
        when(reviewRepository.findAllByGameId(game.getId())).thenReturn((game.getReviews()));


        List<ReviewDto> reviewDtoList = reviewService.getReviewsByGameId(game.getId());

        Assertions.assertEquals(1, reviewDtoList.size());

    }

    @Test
    void deleteReview() {
    }

    @Test
    void updateReview() {
    }

    @Test
    void addReviewToGame() {
    }
}