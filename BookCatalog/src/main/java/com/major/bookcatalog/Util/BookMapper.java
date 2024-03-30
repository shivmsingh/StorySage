package com.major.bookcatalog.Util;

import com.major.bookcatalog.Dto.BookDTO;
import com.major.bookcatalog.Model.AllBooks;
import com.major.bookcatalog.Model.UserBooks;
import com.major.bookcatalog.Repository.UserBooksRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class BookMapper {

    private final UserBooksRepository userBooksRepository;

    public BookMapper(UserBooksRepository userBooksRepository) {
        this.userBooksRepository = userBooksRepository;
    }

    public BookDTO toBookDTO(AllBooks book, String username) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookId(book.getBookId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setSeries(book.getSeries());
        bookDTO.setAuthors(book.getAuthors());
        bookDTO.setRating(book.getRating());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setLanguage(book.getLanguage());
        bookDTO.setGenres(book.getGenres());
        bookDTO.setCharacters(Arrays.asList(book.getCharacters().split(", "))); // Split characters string into a list
        bookDTO.setPublisher(book.getPublisher());
        bookDTO.setAwards(Arrays.asList(book.getAwards().split(", "))); // Split awards string into a list
        bookDTO.setNumRating(book.getNumRating());
        bookDTO.setLikedPercent(book.getLikedPercent());
        bookDTO.setSetting(Arrays.asList(book.getSetting().split(", "))); // Split setting string into a list
        bookDTO.setCoverImg(book.getCoverImg());

        // Check if the book exists in the user's collection
        UserBooks userBook = userBooksRepository.findByUsernameAndBook_BookId(username, book.getBookId());
        if (userBook != null) {
            bookDTO.setStatus(userBook.getStatus());
            bookDTO.setPagesRead(userBook.getPagesRead());
            bookDTO.setTotalPages(userBook.getTotalPages());
            bookDTO.setProgress(userBook.getProgress());
        } else {
            bookDTO.setStatus(null);
            bookDTO.setPagesRead(0);
            bookDTO.setTotalPages(0);
            bookDTO.setProgress(0.0);
        }
        return bookDTO;
    }
}
