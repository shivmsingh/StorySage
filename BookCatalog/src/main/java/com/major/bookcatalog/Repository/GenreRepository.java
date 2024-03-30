package com.major.bookcatalog.Repository;

import com.major.bookcatalog.Model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
