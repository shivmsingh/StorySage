package com.major.identityservice.ErrorHandling;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private String service;

    public ErrorResponse(LocalDateTime timestamp, String message, String service) {
        this.timestamp = timestamp;
        this.message = message;
        this.service = service;
    }

    // Getters and setters
}
