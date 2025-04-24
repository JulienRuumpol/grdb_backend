package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.controller.exceptions.UnauthorizedActionException;
import com.jr.grdb_backend.dto.ReviewDto;
import com.jr.grdb_backend.dto.UpdateReviewDto;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.model.Game;
import com.jr.grdb_backend.model.Review;
import com.jr.grdb_backend.repository.GameRepository;
import com.jr.grdb_backend.repository.ReviewRepository;
import com.jr.grdb_backend.repository.UserRepository;
import com.jr.grdb_backend.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             GameRepository gameRepository,
                             UserRepository userRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ReviewDto> getReviewsByGameId(Long gameId) {
        List<Review> reviews = this.reviewRepository.findAllByGameId(gameId);

        List<ReviewDto> reviewDtos = reviews.stream().map(this::entityToDto).toList();

        return reviewDtos;
    }

    @Transactional
    @Override
    public void deleteReview(Long reviewId) {
        Review review = this.reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("Review with id " + reviewId + " not found"));

        if (isReviewOwner(review) || isAdmin()) {
            this.reviewRepository.delete(review);

        } else {
            throw new UnauthorizedActionException("You do not have permission to delete this review");
        }

    }

    @Override
    public ReviewDto updateReview(Long reviewId, UpdateReviewDto reviewDto) {
        Review review = this.reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("Review with id " + reviewId + " not found"));

        if (isReviewOwner(review)) {
            review.setDescription(reviewDto.getNewDescription());
            review.setRecentlyUpdatedDate(new Date());
            return this.entityToDto(reviewRepository.save(review));
        } else {
            throw new UnauthorizedActionException("You do not have permission to update this review");
        }
    }

    @Transactional
    @Override
    public ReviewDto addReviewToGame(ReviewDto reviewDto) {
        //todo generating overview results in stackoverflow error in user.tostring.
        Review newReview = buildReviewFromDto(reviewDto);
        //todo check date generation. this should probably be in BE and not frontend??

        Review review = this.reviewRepository.save(newReview);
        return entityToDto(review);
    }


    public Review buildReviewFromDto(ReviewDto reviewDto) {
        CustomUser user = userRepository.findById(reviewDto.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Game game = gameRepository.findById(reviewDto.getGameId()).orElseThrow(() -> new RuntimeException("Game with id " + reviewDto.getGameId() + " not found"));

        return Review.builder()
                .id(reviewDto.getId())
                .description(reviewDto.getDescription())
                .user(user)
                .game(game)
                .postedDate(reviewDto.getPostedDate())
                .recentlyUpdatedDate(reviewDto.getRecentlyUpdatedDate())
                .build();
    }

    public ReviewDto entityToDto(Review review) {
        CustomUser user = review.getUser();

        return ReviewDto.builder()
                .id(review.getId())
                .GameId(review.getGame().getId())
                .userId(review.getUser().getId())
                .postedDate(review.getPostedDate())
                .description(review.getDescription())
                .username(user.getUsername())
                .recentlyUpdatedDate(review.getRecentlyUpdatedDate())
                .build();
    }

    @Transactional
    protected Boolean isReviewOwner(Review review) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            CustomUser principalUser = (CustomUser) authentication.getPrincipal();

            CustomUser user = this.userRepository.findByEmail(principalUser.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

            System.out.println(user.getRole());
            return Objects.equals(user.getId(), review.getUser().getId());

        }
        return false;
    }

    @Transactional
    protected Boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            CustomUser principalUser = (CustomUser) authentication.getPrincipal();
            CustomUser user = this.userRepository.findByEmail(principalUser.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

            return user.getRole().getName().equals("Admin");
        }
        return false;
    }
}

