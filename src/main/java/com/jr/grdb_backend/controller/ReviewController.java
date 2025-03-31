package com.jr.grdb_backend.controller;

import com.jr.grdb_backend.dto.ReviewDto;
import com.jr.grdb_backend.dto.UpdateReviewDto;
import com.jr.grdb_backend.model.Review;
import com.jr.grdb_backend.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByGameId(@PathVariable Long gameId) {

        return ResponseEntity.ok(reviewService.getReviewsByGameId(gameId));

    }


    @PostMapping("")
    public ResponseEntity<Review> AddReviewToGame(@RequestBody ReviewDto reviewDto) {

        return ResponseEntity.ok().body(this.reviewService.addReviewToGame(reviewDto));

    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> UpdateReview(@PathVariable Long reviewId, @RequestBody UpdateReviewDto updateReviewDto) {
        return ResponseEntity.ok().body(this.reviewService.updateReview(reviewId, updateReviewDto));



    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity deleteReview(@PathVariable Long reviewId) {

        this.reviewService.deleteReview(reviewId);

        return ResponseEntity.ok().build();
    }


}
