package com.major.bookcatalog.Service;

import com.major.bookcatalog.Dto.BookDTO;
import com.major.bookcatalog.Dto.UserBookRequest;
import com.major.bookcatalog.Model.*;
import com.major.bookcatalog.RecommendationUtils.KafkaProducer;
import com.major.bookcatalog.RecommendationUtils.Payload;
import com.major.bookcatalog.Repository.*;
import com.major.bookcatalog.Util.BookMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@AllArgsConstructor
public class BookService {

    private final AllBooksRepository allBooksRepository;
    private final UserBooksRepository userBooksRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookMapper bookMapper;
    private final KafkaProducer kafkaProducer;

    public List<BookDTO> getBooksByUsername(String username, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<AllBooks> allBooksPage = allBooksRepository.findAll(pageable).getContent();
        List<BookDTO> bookDTOs = new ArrayList<>();
        log.info("Inside getBooksByUsername");
        for (AllBooks book : allBooksPage) {
            BookDTO bookDTO = bookMapper.toBookDTO(book, username);
            bookDTOs.add(bookDTO);
        }

        return bookDTOs;
    }


    public void addUserBook(UserBookRequest request) {
        AllBooks allBooks = allBooksRepository.findById(request.getBookId()).orElse(null);
        if (allBooks != null) {
            UserBooks userBook = new UserBooks();
            userBook.setBook(allBooks);
            userBook.setUsername(request.getUsername());
            userBook.setTotalPages(request.getTotalPages());
            userBook.setPagesRead(request.getPagesRead());
            userBook.setStatus(request.getStatus());

            userBooksRepository.save(userBook);
            Payload payload = new Payload(request.getUsername(),request.getBookId(),"SAVE");
            kafkaProducer.publishRecommendation(payload);
        }
    }

    public List<AllBooks> getMyBooks(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<UserBooks> userBooks = userBooksRepository.findUserBooksByUsername(username, pageable).getContent();
        List<AllBooks> allBooks = new ArrayList<>();
        for (UserBooks userBook : userBooks) {
            allBooks.add(userBook.getBook());
        }
        return allBooks;
    }

    public BookDTO getBooksByUsernameAndBookId(String username, Long id) {
        Optional<AllBooks> book = allBooksRepository.findById(id);
        if(book.isPresent()){
            BookDTO bookDTO = new BookDTO();
            bookDTO.setBookId(book.get().getBookId());
            bookDTO.setTitle(book.get().getTitle());
            bookDTO.setSeries(book.get().getSeries());
            bookDTO.setAuthors(book.get().getAuthors());
            bookDTO.setRating(book.get().getRating());
            bookDTO.setDescription(book.get().getDescription());
            bookDTO.setLanguage(book.get().getLanguage());
            bookDTO.setGenres(book.get().getGenres());
            bookDTO.setCharacters(Arrays.asList(book.get().getCharacters().split(", "))); // Split characters string into a list
            bookDTO.setPublisher(book.get().getPublisher());
            bookDTO.setAwards(Arrays.asList(book.get().getAwards().split(", "))); // Split awards string into a list
            bookDTO.setNumRating(book.get().getNumRating());
            bookDTO.setLikedPercent(book.get().getLikedPercent());
            bookDTO.setSetting(Arrays.asList(book.get().getSetting().split(", "))); // Split setting string into a list
            bookDTO.setCoverImg(book.get().getCoverImg());

            // Check if the book exists in the user's collection
            UserBooks userBook = userBooksRepository.findByUsernameAndBook_BookId(username, book.get().getBookId());
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
       return null;
    }

    public List<BookDTO> getBooksByGenre(String username, int page, int size, Long id) {
        Pageable pageable = PageRequest.of(page, size);
        List<AllBooks> BooksByGenreId = allBooksRepository.findByGenreId(id, pageable);
        List<BookDTO> bookDTOs = new ArrayList<>();
        log.info("Inside getBooksByGenre");
        for (AllBooks book : BooksByGenreId) {
            BookDTO bookDTO = bookMapper.toBookDTO(book, username);
            bookDTOs.add(bookDTO);
        }
        return bookDTOs;

    }

    public List<BookDTO> getBooksByAuthor(String username, int page, int size, Long id) {
        Pageable pageable = PageRequest.of(page, size);
        List<AllBooks> BooksByAuthorId = allBooksRepository.findByAuthorId(id, pageable);
        List<BookDTO> bookDTOs = new ArrayList<>();
        log.info("Inside getBooksByAuthor");
        for (AllBooks book : BooksByAuthorId) {
            BookDTO bookDTO = bookMapper.toBookDTO(book, username);
            bookDTOs.add(bookDTO);
        }
        return bookDTOs;
    }


    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Transactional
    public void deleteBook(String username, Long bookId) {
        userBooksRepository.deleteByUsernameAndBook_BookId(username,bookId);
        Payload payload = new Payload(username,bookId,"DELETE");
        kafkaProducer.publishRecommendation(payload);
    }

}
