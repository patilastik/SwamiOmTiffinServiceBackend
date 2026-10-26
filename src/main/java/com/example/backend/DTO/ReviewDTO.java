package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {

    private String email;
    private String firstName;
    private String lastName;
    private String message;
    private short stars; // out of 5
}