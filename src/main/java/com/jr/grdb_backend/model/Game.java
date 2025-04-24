package com.jr.grdb_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
//    private byte[] image;


    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();


    public void addReviewToGame(Review review) {
        this.reviews.add(review);
    }


    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", reviews=" + appendAllReviews() +
                '}';
    }

    private String appendAllReviews() {
        StringBuilder reviewsString = new StringBuilder();

        for (Review review : reviews) {
            String username = review.getUser().getUsername();
            String description = review.getDescription();
            String date = review.getPostedDate().toString();
            reviewsString.append(username).append(" ").append(date).append(" ").append(description);

        }
        return reviewsString.toString();
    }
}
