// Each single object of this class contains a single review information
package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewsDTO {
    private String email;
    private String name;
    private String message;
    private short stars;
    private LocalDateTime createdAt;
}