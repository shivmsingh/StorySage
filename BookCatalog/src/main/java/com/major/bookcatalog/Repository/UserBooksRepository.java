package com.major.bookcatalog.Repository;

import com.major.bookcatalog.Model.UserBooks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserBooksRepository extends JpaRepository<UserBooks, Long> {
    Page<UserBooks> findUserBooksByUsername(String username, Pageable page);
    UserBooks findByUsernameAndBook_BookId(String username, Long bookId);

    void deleteByUsernameAndBook_BookId(String username, Long bookId);
}
