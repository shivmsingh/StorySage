package com.major.bookcatalog.Repository;

import com.major.bookcatalog.Model.AllBooks;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllBooksRepository extends JpaRepository<AllBooks, Long> {
    @Query("SELECT b FROM AllBooks b JOIN b.authors a WHERE a.id = :authorId")
    List<AllBooks> findByAuthorId(@Param("authorId") Long authorId, Pageable page);

    @Query("SELECT b FROM AllBooks b JOIN b.genres g WHERE g.id = :genreId")
    List<AllBooks> findByGenreId(@Param("genreId") Long genreId, Pageable pageable);
}
