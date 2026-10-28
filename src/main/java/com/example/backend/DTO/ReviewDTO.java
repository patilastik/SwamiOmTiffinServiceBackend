package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ReviewDTO {

    private String email;
    private String firstName;
    private String lastName;
    private String message;
    private short stars; // out of 5
}