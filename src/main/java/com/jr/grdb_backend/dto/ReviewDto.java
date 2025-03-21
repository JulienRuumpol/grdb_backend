package com.jr.grdb_backend.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class ReviewDto {
    private Long GameId;
    private Long userId;
    private String description;
    private Date postedDate;
}

