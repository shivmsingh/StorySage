package com.major.bookcatalog.Repository;

import com.major.bookcatalog.Model.UserBooks;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserBooksRepository extends JpaRepository<UserBooks, Long> {
    Page<UserBooks> findUserBooksByUsername(String username, Pageable page);
    UserBooks findByUsernameAndBook_BookId(String username, Long bookId);
    void deleteByUsernameAndBook_BookId(String username, Long bookId);
    List<UserBooks> findAllByUsernameAndStatus(String username, String status, Pageable page);

    @Modifying
    @Transactional
    @Query("UPDATE UserBooks u " +
            "SET u.totalPages = :totalPages, u.pagesRead = :pagesRead, " +
            "u.status = :status, u.stars = :stars " +
            "WHERE u.username = :username AND u.book.bookId = :bookId")
    void updateUserBook(@Param("username") String username, @Param("bookId") Long bookId,
                             @Param("totalPages") int totalPages, @Param("pagesRead") int pagesRead,
                             @Param("status") String status, @Param("stars") double stars);
}
