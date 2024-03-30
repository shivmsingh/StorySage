package com.major.bookcatalog.Repository;

import com.major.bookcatalog.Model.BooksGenres;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BooksGenresRepository extends JpaRepository<BooksGenres, Long> {
    List<BooksGenres> findBooksGenresByGenre_Id(Long Id,  Pageable page);
}
