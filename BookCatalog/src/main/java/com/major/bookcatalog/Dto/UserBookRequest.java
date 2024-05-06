package com.major.bookcatalog.Dto;

import lombok.Data;

@Data
public class UserBookRequest {
    private String username;
    private Long bookId;
    private int totalPages = 0;
    private int pagesRead = 0;
    private String status = "Want to read";
    private Double stars = 0.0;
}
