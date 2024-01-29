package ru.gb.springdemo.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.repository.BookRepository;

import java.util.List;

@Service
@Data
@NoArgsConstructor
public class BookService {
    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    public Book saveBook(Book book) {
        bookRepository.save(book);
        return book;
    }
}
