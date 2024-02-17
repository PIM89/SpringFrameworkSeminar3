package ru.gb.springdemo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.annotations.NotFound;
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
        List<Book> books = bookService.findAll();
        if (!books.isEmpty()) {
            return ResponseEntity.ok(books);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Возвращает информацию о книге с заданным идентификатором")
    public ResponseEntity<Book> getBookById(@PathVariable long id) {
        return bookService.findById(id)
                .map(it -> ResponseEntity.ok(it))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book by ID", description = "Удаляет книгу из БД с заданным идентификатором")
    public ResponseEntity<Book> deleteBookById(@PathVariable long id) {
        if (bookService.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            bookService.deleteById(id);
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/add/{name}")
    @Operation(summary = "Add book", description = "Добавляет новую книгу в БД")
    public ResponseEntity<Book> addBook(@PathVariable String name) {
        Book book = new Book(name);
        try {
            bookService.saveBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(book);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }
}
