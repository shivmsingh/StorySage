package com.major.bookcatalog.Repository;

import com.major.bookcatalog.Model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
