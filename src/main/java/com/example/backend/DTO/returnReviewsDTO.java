package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReturnReviewsDTO {
    private String email;
    private String name;
    private String message;
    private short stars;
    private LocalDateTime createdAt;
}