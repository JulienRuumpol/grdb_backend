package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.dto.ReviewDto;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.model.Game;
import com.jr.grdb_backend.model.Review;
import com.jr.grdb_backend.repository.GameRepository;
import com.jr.grdb_backend.repository.ReviewRepository;
import com.jr.grdb_backend.repository.UserRepository;
import com.jr.grdb_backend.service.ReviewService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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

        List<ReviewDto> reviewDtos =  reviews.stream().map(this::entityToDto).toList();

        return reviewDtos;
    }

    @Transactional
    @Override
    public Review addReviewToGame(ReviewDto reviewDto) {
        //todo generating overview results in error in user.tostring.
        Review newReview = buildReviewFromDto(reviewDto);

        return reviewRepository.save(newReview);
    }




    private Review buildReviewFromDto(ReviewDto reviewDto) {
        CustomUser user = userRepository.findById(reviewDto.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Game game = gameRepository.findById(reviewDto.getGameId()).orElseThrow(() -> new RuntimeException("Game with id " + reviewDto.getGameId() + " not found"));

        return Review.builder()
                .description(reviewDto.getDescription())
                .user(user)
                .game(game)
                .postedDate(reviewDto.getPostedDate())
                .build();
    }

    private ReviewDto entityToDto(Review review){

        return ReviewDto.builder()
                .GameId(review.getGame().getId())
                .userId(review.getUser().getId())
                .postedDate(review.getPostedDate())
                .description(review.getDescription())
                .build();

    }
}
