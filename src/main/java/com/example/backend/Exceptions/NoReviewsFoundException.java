package com.example.backend.exceptions;

public class NoReviewsFoundException extends RuntimeException {

    public NoReviewsFoundException() {
        super("No reviews found");
    }

    public NoReviewsFoundException(String message) {
        super(message);
    }
}