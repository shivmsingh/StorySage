    package com.major.bookcatalog.Model;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.ToString;

    import java.util.List;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "all_books")
    public class AllBooks {

        @Id
        @Column(name = "bookId")
        private Long bookId;

        @Column(name = "title", length = 300, nullable = false)
        private String title;

        @Column(name = "series", length = 300)
        private String series;

        @Column(name = "rating")
        private Double rating;

        @Column(name = "description")
        private String description;

        @Column(name = "language", length = 50)
        private String language;

        @ToString.Exclude
        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(
                name = "books_authors",
                joinColumns = @JoinColumn(name = "book_id"),
                inverseJoinColumns = @JoinColumn(name = "author_id")
        )
        private List<Author> authors;

        @ToString.Exclude
        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(
                name = "books_genres",
                joinColumns = @JoinColumn(name = "book_id"),
                inverseJoinColumns = @JoinColumn(name = "genre_id")
        )
        private List<Genre> genres;

        @Column(name = "characters")
        private String characters;


        @Column(name = "publisher")
        private String publisher;


        @Column(name = "awards")
        private String awards;

        @Column(name = "numRating")
        private Long numRating;

        @Column(name = "likedPercent")
        private Integer likedPercent;


        @Column(name = "setting")
        private String setting;

        @Lob
        @Column(name = "coverImg")
        private String coverImg;

        // Getters and setters
    }
