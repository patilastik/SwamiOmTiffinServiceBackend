package com.example.backend.exceptions;

public class InvalidValueException extends RuntimeException {

    public InvalidValueException(){
        super("Invalid value provided");
    }
    public InvalidValueException(String message) {
        super(message);
    }
}