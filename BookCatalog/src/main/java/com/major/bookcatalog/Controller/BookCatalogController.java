package com.major.bookcatalog.Controller;


import com.major.bookcatalog.Dto.BookDTO;
import com.major.bookcatalog.Dto.UserBookRequest;
import com.major.bookcatalog.Model.*;
import com.major.bookcatalog.Service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookcatalog")
public class BookCatalogController {

    @Autowired
    private BookService bookCatalogService;

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

    @PostMapping("/my-books")
    public void addUserBook(
            HttpServletRequest request, @RequestBody UserBookRequest userBookRequest) {
        String username = request.getHeader("username");
        userBookRequest.setUsername(username);
        bookCatalogService.addUserBook(userBookRequest);
    }

    @GetMapping("/my-books")
    public List<UserBooks> getUserBookCatalog(
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
