package com.major.identityservice.ErrorHandling;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
