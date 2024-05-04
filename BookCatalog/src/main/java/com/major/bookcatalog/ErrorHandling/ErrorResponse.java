package com.major.bookcatalog.ErrorHandling;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private String service;
    private int code;

    public ErrorResponse(LocalDateTime timestamp, String message, String service, int code) {
        this.timestamp = timestamp;
        this.message = message;
        this.service = service;
        this.code = code;
    }

    // Getters and setters
}
