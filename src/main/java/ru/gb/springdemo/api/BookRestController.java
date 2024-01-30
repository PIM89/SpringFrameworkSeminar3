package ru.gb.springdemo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Book")
public class BookRestController {

    private BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Возвращает список всех имеющихся книг в БД")
    public ResponseEntity<List<Book>> getAllBooks() {
        try {
            List<Book> books = bookService.findAll();
            return ResponseEntity.status(HttpStatus.FOUND).body(books);
        } catch (NullPointerException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Возвращает информацию о книге с заданным идентификатором")
    public Book getBookById(@PathVariable long id) {
        return bookService.findById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book by ID", description = "Удаляет книгу из БД с заданным идентификатором")
    public List<Book> deleteBookById(@PathVariable long id) {
        bookService.deleteById(id);
        return bookService.findAll();
    }

    @PostMapping("/add/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add book", description = "Добавляет новую книгу в БД")
    public Book addBook(@PathVariable String name) {
        Book book = new Book(name);
        bookService.saveBook(book);
        return book;
    }
}
