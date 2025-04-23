package com.jr.grdb_backend.service;

import com.jr.grdb_backend.dto.ReviewDto;
import com.jr.grdb_backend.dto.UpdateReviewDto;

import java.util.List;

public interface ReviewService  {

    ReviewDto addReviewToGame(ReviewDto reviewDto);
    List<ReviewDto> getReviewsByGameId(Long gameId);
    void deleteReview(Long reviewId);
    ReviewDto updateReview(Long reviewId, UpdateReviewDto reviewDto);
}
