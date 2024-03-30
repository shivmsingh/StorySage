package com.major.bookcatalog.Dto;

import com.major.bookcatalog.Model.Author;
import com.major.bookcatalog.Model.Genre;
import lombok.Data;

import java.util.List;

@Data
public class BookDTO {
    private Long bookId;
    private String title;
    private String series;
    private List<Author> authors;
    private Double rating;
    private String description;
    private String language;
    private List<Genre> genres;
    private List<String> characters;
    private String publisher;
    private List<String> awards;
    private Long numRating;
    private Integer likedPercent;
    private List<String> setting;
    private String coverImg;
    private String status;
    private int totalPages;
    private int pagesRead;
    private double progress;
}
