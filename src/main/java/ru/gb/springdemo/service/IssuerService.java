package ru.gb.springdemo.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.api.IssueRequest;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.repository.IssuerRepository;
import ru.gb.springdemo.repository.ReaderRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Data
@NoArgsConstructor
public class IssuerService {
    private BookRepository bookRepository;
    private ReaderRepository readerRepository;
    private IssuerRepository issuerRepository;
    @Value("${application.max-allowed-books:1}")
    private Integer maxAllowedBooks;

    @Autowired
    IssuerService(BookRepository bookRepository, ReaderRepository readerRepository, IssuerRepository issuerRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.issuerRepository = issuerRepository;
    }

    public List<Issue> findAll() {
        return issuerRepository.findAll();
    }

    public Issue findIssueById(Long id) {
        return issuerRepository.findById(id).get();
    }

    public List<Issue> findAllByIssueTimestampNotNull(){
        return issuerRepository.findAllByIssueTimestampNotNull();
    }


    public Issue issuance(IssueRequest issueRequest) {
        Optional<Book> currentBook = bookRepository.findById(issueRequest.getBookId());
        Optional<Reader> currentReader = readerRepository.findById(issueRequest.getReaderId());

        if (currentBook.isEmpty()) {
            throw new NullPointerException("Не найдена книга с id \"" + issueRequest.getBookId() + "\"");
        }
        if (!currentBook.get().getOnStorage()) {
            throw new NullPointerException("Книга с id \"" + issueRequest.getBookId() + "\" на выдаче");
        }
        if (currentReader.isEmpty()) {
            throw new NullPointerException("Не найден читатель с id \"" + issueRequest.getReaderId() + "\"");
        }
        if (currentReader.get().getCountBooks() >= maxAllowedBooks) {
            throw new NoSuchElementException("Превышен максимальный лимит книг на выдачу читателю с id \"" + issueRequest.getReaderId() + "\"");
        }
        Issue issue = new Issue(currentBook.get(), currentReader.get());
        currentReader.get().setCountBooks(currentReader.get().getCountBooks() + 1);
        currentBook.get().setOnStorage(false);
        issuerRepository.save(issue);
        return issue;
    }

    public Book returnBook(Long bookId) {
        Optional<Book> currentBook = bookRepository.findById(bookId);
        Optional<Issue> currentIssue = issuerRepository.findIssueByBookId(bookId);
        Optional<Reader> currentReader = readerRepository.findById(currentIssue.get().getReader().getId());

        if (currentBook.isEmpty()) {
            throw new NullPointerException("Не найдена книга с id \"" + bookId + "\"");
        }
        if (currentBook.get().getOnStorage()) {
            throw new NoSuchElementException("Книга с id \"" + bookId + "\" не выдавалась!");
        }

        currentIssue.get().setReturnTimestamp(LocalDateTime.now());
        issuerRepository.save(currentIssue.get());

        currentBook.get().setOnStorage(true);
        bookRepository.save(currentBook.get());

        currentReader.get().setCountBooks(currentReader.get().getCountBooks() - 1);
        readerRepository.save(currentReader.get());
        return currentBook.get();
    }

    public List<Issue> getListBookByReader(long idReader) {
        return issuerRepository.findAllIssueByReaderIdAndIssueTimestampNotNull(idReader);
    }
}
