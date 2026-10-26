package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Reviewer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewer_id;
    private String email;
    private String firstName;
    private String lastName;
    private String message;
    private short stars;

    public Reviewer(String email, String firstName, String lastName, String message, short stars) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.message = message;
        this.stars = stars;
    }
}