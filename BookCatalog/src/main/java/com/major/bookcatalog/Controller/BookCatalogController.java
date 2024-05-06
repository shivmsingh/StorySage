package com.major.bookcatalog.Controller;


import com.major.bookcatalog.Dto.AllBooksDTO;
import com.major.bookcatalog.Dto.BookDTO;
import com.major.bookcatalog.Dto.UserBookRequest;
import com.major.bookcatalog.ErrorHandling.BadRequestException;
import com.major.bookcatalog.Model.*;
import com.major.bookcatalog.Service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins ="*")
@RestController
@RequestMapping("/bookcatalog")
public class BookCatalogController {


    private final BookService bookCatalogService;

    public BookCatalogController(BookService bookCatalogService) {
        this.bookCatalogService = bookCatalogService;
    }

    @GetMapping("/user-books")
    public List<BookDTO> getBookCatalog(
            @RequestHeader("username") String username,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "0") int pageNo) {
        return bookCatalogService.getBooksByUsername(username, pageNo, pageSize);
    }

    @GetMapping("/user-books/book/{id}")
    public BookDTO getBookById(
            @RequestHeader("username") String username,
            @PathVariable Long id) {
        return bookCatalogService.getBooksByUsernameAndBookId(username,id);
    }

    @GetMapping("/user-books/books/title/{title}")
    public List<BookDTO> getBooksByTitle(
            @RequestHeader("username") String username,
            @PathVariable String title,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "0") int pageNo) {
        return bookCatalogService.getBooksByUsernameAndTitle(username,title, pageNo, pageSize);
    }


    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return bookCatalogService.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public List<BookDTO> getBooksByGenre(
            @RequestHeader("username") String username,
            @PathVariable Long id,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "0") int pageNo) {
        return bookCatalogService.getBooksByGenre(username, pageNo, pageSize, id);
    }

    @GetMapping("/authors")
    public List<Author> getAllAuthors() {
        return bookCatalogService.getAllAuthors();
    }

    @GetMapping("/authors/{id}")
    public List<BookDTO> getBooksByAuthor(
            @RequestHeader("username") String username,
            @PathVariable Long id,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "0") int pageNo) {
        return bookCatalogService.getBooksByAuthor(username, pageNo, pageSize, id);
    }

    @GetMapping("/authors/details/{id}")
    public Author getAuthorBio(
            @PathVariable Long id) {
        return bookCatalogService.getAuthorDetails(id);
    }

    @GetMapping("/genres/details/{id}")
    public Genre getGenresBio(
            @PathVariable Long id) {
        return bookCatalogService.getGenreDetails(id);
    }

    @PostMapping("/my-books")
    public ResponseEntity<UserBooks> addUserBook(
            HttpServletRequest request, @RequestBody UserBookRequest userBookRequest) {

        if(userBookRequest.getPagesRead() < 0 || userBookRequest.getTotalPages() < 0) {
            throw new BadRequestException("Page number and total number must be greater than 0");
        }

        if(userBookRequest.getPagesRead() > userBookRequest.getTotalPages()){
            throw new BadRequestException("Pages Read cannot be greater than Total Pages");
        }

        String username = request.getHeader("username");
        userBookRequest.setUsername(username);
        UserBooks userBook = bookCatalogService.addUserBook(userBookRequest);
        if (userBook != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userBook);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/my-books/{bookId}")
    public ResponseEntity<Void> updateUserBook(
            HttpServletRequest request, @RequestBody UserBookRequest userBookRequest, @PathVariable Long bookId) {

        String username = request.getHeader("username");
        userBookRequest.setUsername(username);
        userBookRequest.setBookId(bookId);

        if (userBookRequest.getPagesRead() < 0 || userBookRequest.getTotalPages() < 0) {
            throw new BadRequestException("Page number and total number must be greater than 0");
        }

        if (userBookRequest.getPagesRead() > userBookRequest.getTotalPages()) {
            throw new BadRequestException("Pages Read cannot be greater than Total Pages");
        }

        bookCatalogService.updateUserBook(userBookRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    @GetMapping("/my-books")
    public List<AllBooksDTO> getUserBookCatalog(
            @RequestHeader("username") String username,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "0") int pageNo) {
        return bookCatalogService.getMyBooks(username,pageNo,pageSize);
    }

    @DeleteMapping("/my-books/{bookId}")
    public void deleteBook(@RequestHeader("username") String username, @PathVariable Long bookId){
        bookCatalogService.deleteBook(username, bookId);
    }
}
