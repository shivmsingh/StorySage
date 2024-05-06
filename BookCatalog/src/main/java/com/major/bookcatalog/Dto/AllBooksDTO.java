package com.major.bookcatalog.Dto;

import com.major.bookcatalog.Model.Author;
import com.major.bookcatalog.Model.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllBooksDTO {
    private Long bookId;
    private String title;
    private Double rating;
    private List<Author> authors;
    private List<Genre> genres;
    private String coverImg;
    private int totalPages;
    private int pagesRead;
    private double progress;
    private String status;
    private Double stars;

}
