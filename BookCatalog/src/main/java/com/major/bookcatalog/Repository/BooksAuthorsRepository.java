package com.major.bookcatalog.Repository;

import com.major.bookcatalog.Model.BooksAuthors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BooksAuthorsRepository extends JpaRepository<BooksAuthors, Long> {
    List<BooksAuthors> findBooksAuthorsByAuthor_Id(Long id, Pageable page);
}
