package com.jr.grdb_backend.dto;

import lombok.Data;

@Data
public class GameDto {
    private Long id;
    private String name;
    private String description;
    private String imageRef;
//    private byte[] image;
}
