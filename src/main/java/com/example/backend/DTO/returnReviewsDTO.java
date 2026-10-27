package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class returnReviewsDTO {
    private String email;
    private String name;
    private String message;
    private short stars;
    private LocalDateTime createdAt;
}