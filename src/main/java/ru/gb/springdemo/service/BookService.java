package ru.gb.springdemo.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.repository.BookRepository;

import java.util.List;

@Service
@Data
public class BookService {
    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks(){
        return bookRepository.getAllBooks();
    }

    public Book getBookById(long id){
        return bookRepository.getBookById(id);
    }

    public void deleteBookById(long id){
        bookRepository.deleteBookById(id);
    }

    public Book addBook(Book book){
        return  bookRepository.addBook(book);
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.getAvailableBooks();
    }

}
