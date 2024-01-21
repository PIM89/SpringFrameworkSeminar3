package ru.gb.springdemo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Метод возвращает список всех книг
     * @return
     */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    /**
     * Метод возвращает информацию о книге с заданным id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable long id) {
        return bookService.getBookById(id);
    }

    /**
     * Метод удаляет книгу с заданным id и возвращает список оставшихся в репозитории книг
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public List<Book> deleteBookById(@PathVariable long id) {
        bookService.deleteBookById(id);
        return bookService.getAllBooks();
    }

    /**
     * Метод добавляет книгу в репозиторий и возвращает её
     * @param
     * @return
     */
    @PostMapping("/add/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    public Book addBook(@PathVariable String name) {
        bookService.addBook(new Book(name));
        return bookService.getAllBooks().getLast();
    }
}
