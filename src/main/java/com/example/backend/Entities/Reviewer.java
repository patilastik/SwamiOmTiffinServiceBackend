package com.example.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Reviewer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewer_id;

    @Column(unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "message")
    private String message;

    @Column(name = "stars")
    private short stars;

    public Reviewer(String email, String firstName, String lastName, String message, short stars) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.message = message;
        this.stars = stars;
    }
}