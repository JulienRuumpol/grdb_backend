package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.dto.ReviewDto;
import com.jr.grdb_backend.dto.UpdateReviewDto;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.model.Game;
import com.jr.grdb_backend.model.Review;
import com.jr.grdb_backend.model.Role;
import com.jr.grdb_backend.repository.GameRepository;
import com.jr.grdb_backend.repository.ReviewRepository;
import com.jr.grdb_backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Important!

class ReviewServiceImplTest {


    @InjectMocks
    private ReviewServiceImpl reviewService;
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private GameRepository gameRepository;
    @Mock
    private UserRepository userRepository;
    private Review review;
    private Game game;
    private CustomUser user;

//    public ReviewServiceImplTest(ReviewRepository reviewRepository,
//                                 GameRepository gameRepository,
//                                 UserRepository userRepository
//                                 ) {
//        this.reviewRepository =reviewRepository;
//        this.gameRepository = gameRepository;
//        this.userRepository =  userRepository;
//    }

    @BeforeEach
    void setUp() {


        this.game = new Game();
        this.game.setId(1l);
        this.game.setDescription("a new testing game");
        this.game.setName("testing the game");

        this.user = new CustomUser();
        this.user.setId(1l);
        this.user.addGameToGames(game);

        this.review = new Review();
        this.review.setId(1l);
        this.review.setDescription("testing");
        this.review.setPostedDate(new Date());
        this.review.setGame(game);
        this.review.setUser(user);
        this.game.addReviewToGame(this.review);
    }

    /**
     * Unit test {@link ReviewServiceImpl#getReviewsByGameId(Long)}
     * Scenario: Successfully retrieve reviews by ID
     */

    @Test
    void getReviewsByGameId() {
        when(reviewRepository.findAllByGameId(game.getId())).thenReturn((game.getReviews()));


        List<ReviewDto> reviewDtoList = reviewService.getReviewsByGameId(game.getId());

        Assertions.assertEquals(1, reviewDtoList.size());

    }
    /**
     * Unit test {@link ReviewServiceImpl#deleteReview(Long)}
     * Scenario: Successfully delete review by ID
     */
    @Test
    void deleteReview() {
        when(reviewRepository.findAllByGameId(game.getId())).thenReturn((game.getReviews()));
        when(this.reviewRepository.findById(this.review.getId())).thenReturn(Optional.ofNullable(this.review));

        this.reviewService.deleteReview(this.review.getId());

        verify(reviewRepository, times(1)).deleteById(this.review.getId());

    }

/**
 * Unit test {@link ReviewServiceImpl#deleteReview(Long)}
 * Scenario: Unable to delete review due to not being authorized to do so
 */
    @Test
    void unauthorizedToDeleteReview(){

    }
    /**
     * Unit test {@link ReviewServiceImpl#updateReview(Long, UpdateReviewDto)}
     * Scenario: Successfully update a review
     */
    @Test
    void updateReview() {
        String updateText = "this has been updated";
        when(this.reviewRepository.findById(this.review.getId())).thenReturn(Optional.ofNullable(this.review));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateReviewDto updateReviewDto = new UpdateReviewDto(updateText);

        ReviewDto dto = this.reviewService.updateReview(this.review.getId(), updateReviewDto);
        verify(this.reviewRepository, times(1)).findById(this.review.getId());

        Review updatedReviewExample = this.review;
        updatedReviewExample.setDescription("");


    }

    @Test
    void addReviewToGame() {
    }


    private void addRoleToUser(String roleName){
        Role role  = new Role();
        role.setId(1l);
        role.setName(roleName);
        this.user.setRole(role);
    }
}