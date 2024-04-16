package com.major.identityservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomResponse {
    private int code;
    private String message;
    private LocalDateTime timestamp;

    public CustomResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters
    // Omitted for brevity, you can generate them using your IDE or lombok
}
