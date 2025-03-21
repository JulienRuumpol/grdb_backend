package com.jr.grdb_backend.repository;

import com.jr.grdb_backend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

//    List<Review> findAllByGameEquals(Long gameId);
    List<Review> findAllByGameId(Long gameId);

}
