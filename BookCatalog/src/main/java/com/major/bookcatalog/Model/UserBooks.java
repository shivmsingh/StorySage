package com.major.bookcatalog.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBooks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "bookId")
    @ToString.Exclude
    private AllBooks book;

    @Column(name = "total_pages")
    private int totalPages;

    @Column(name = "pages_read")
    private int pagesRead;

    @Column(name = "progress")
    private double progress;


    @Column(name = "status")
    private String status;

    @Column
    private Double stars;
    // Constructor without id and progress
    public UserBooks(String username, AllBooks book, int totalPages, int pagesRead, String status) {
        this.username = username;
        this.book = book;
        this.totalPages = totalPages;
        this.pagesRead = pagesRead;
        this.status = status;

    }

    // Method to calculate progress based on pages read and total pages
    public void calculateProgress() {
        if (totalPages == 0) {
            this.progress = 0.0; // Avoid division by zero
        } else {
            this.progress = ((double) pagesRead / totalPages) * 100.0;
        }
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        calculateProgress();
    }

    public void setPagesRead(int pagesRead) {
        this.pagesRead = pagesRead;
        calculateProgress();
    }
}
