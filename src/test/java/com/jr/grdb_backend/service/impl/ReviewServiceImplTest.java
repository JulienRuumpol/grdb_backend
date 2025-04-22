package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.controller.exceptions.UnauthorizedActionException;
import com.jr.grdb_backend.dto.ReviewDto;
import com.jr.grdb_backend.dto.UpdateReviewDto;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.model.Game;
import com.jr.grdb_backend.model.Review;
import com.jr.grdb_backend.model.Role;
import com.jr.grdb_backend.repository.GameRepository;
import com.jr.grdb_backend.repository.ReviewRepository;
import com.jr.grdb_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    private Role role;


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

        assertEquals(1, reviewDtoList.size());

    }

    /**
     * Unit test {@link ReviewServiceImpl#deleteReview(Long)}
     * Scenario: Successfully delete review by ID
     */
    @Test
    void deleteReview() {
        //todo figure out stackoverflow error in this test
//        when(reviewRepository.findAllByGameId(game.getId())).thenReturn((game.getReviews()));
        when(this.reviewRepository.findById(this.review.getId())).thenReturn(Optional.ofNullable(this.review));

        this.reviewService.deleteReview(this.review.getId());

//        verify(reviewRepository, times(1)).deleteById(this.review.getId());

    }

    /**
     * Unit test {@link ReviewServiceImpl#deleteReview(Long)}
     * Scenario: Unable to delete review due to not being authorized to do so
     */
    @Test
    void unauthorizedToDeleteReview() {
        Review newReview = this.review;
        CustomUser newUser = this.user;
        Role newRole = this.role;
        newRole.setName("Basic");
        newUser.setRole(newRole);
        newReview.setUser(newUser);

        when(reviewRepository.findById(game.getId())).thenReturn(Optional.of((newReview)));

        Exception exception = assertThrows(UnauthorizedActionException.class, () -> {
            this.reviewService.deleteReview(newReview.getId());
        });

        assertEquals("You do not have permission to delete this review", exception.getMessage());

    }

    /**
     * Unit test {@link ReviewServiceImpl#updateReview(Long, UpdateReviewDto)}
     * Scenario: Successfully update a review
     */
    @Test
    void updateReview() {
        String updateText = "this has been updated";
        when(this.reviewRepository.findById(this.review.getId())).thenReturn(Optional.ofNullable(this.review));
        when(this.reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateReviewDto updateReviewDto = new UpdateReviewDto(updateText);

        ReviewDto dto = this.reviewService.updateReview(this.review.getId(), updateReviewDto);

        Review updatedReviewExample = this.review;
        updatedReviewExample.setDescription(updateText);

        verify(this.reviewRepository, times(1)).findById(this.review.getId());
        assertEquals(dto, this.reviewService.entityToDto(updatedReviewExample));
    }

    /**
     * Unit test {@link ReviewServiceImpl#updateReview(Long, UpdateReviewDto)}
     * Scenario: Can't update a review due to authorization issue
     */

    @Test
    void unAuthorizedToUpdateReview() {
        Review newReview = this.review;
        CustomUser newUser = this.user;
        Role newRole = this.role;
        newRole.setName("Basic");
        newUser.setRole(newRole);
        newReview.setUser(newUser);
        String updateText = "this has been updated";

        UpdateReviewDto updateReviewDto = new UpdateReviewDto(updateText);

        when(this.reviewRepository.findById(this.review.getId())).thenReturn(Optional.of(newReview));

        Exception exception = assertThrows(UnauthorizedActionException.class, () -> {
            this.reviewService.updateReview(this.review.getId(), updateReviewDto);
        });

        assertEquals("You do not have permission to update this review", exception.getMessage());

    }

    @Test
    void addReviewToGame() {
        ReviewDto newReviewDto = ReviewDto.builder()
                .GameId(1l)
                .id(2l)
                .postedDate(new Date())
                .description("a new review")
                .username(this.user.getUsername())
                .userId(this.user.getId())
                .build();
        when(this.userRepository.findById(newReviewDto.getUserId())).thenReturn(Optional.ofNullable(this.user));
        when(this.gameRepository.findById(newReviewDto.getGameId())).thenReturn(Optional.ofNullable(this.game));

        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReviewDto addedReview = this.reviewService.addReviewToGame(newReviewDto);

        assertEquals(addedReview, newReviewDto);

    }


    private void addRoleToUser(String roleName) {
        Role role = new Role();
        role.setId(1l);
        role.setName(roleName);
        this.user.setRole(role);
    }
}