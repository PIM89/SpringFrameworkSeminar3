package ru.gb.springdemo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.service.BookService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/book")
public class BookRestController {

    private BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        try {
            List<Book> books = bookService.findAll();
            return ResponseEntity.status(HttpStatus.FOUND).body(books);
        } catch (NullPointerException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable long id) {
        return bookService.findById(id);
    }

    @DeleteMapping("/{id}")
    public List<Book> deleteBookById(@PathVariable long id) {
        bookService.deleteById(id);
        return bookService.findAll();
    }

    @PostMapping("/add/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    public Book addBook(@PathVariable String name) {
        Book book = new Book(name);
        bookService.saveBook(book);
        return book;
    }
}
