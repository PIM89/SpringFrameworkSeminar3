package ru.gb.springdemo.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.api.IssueRequest;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.repository.IssueRepository;
import ru.gb.springdemo.repository.ReaderRepository;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Data
public class IssuerService {
    // спринг это все заинжектит
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final IssueRepository issueRepository;
    @Value("${application.max-allowed-books:1}")
    private int maxAllowedBooks;

    public List<Issue> getAllIssue() {
        return List.copyOf(issueRepository.getIssues());
    }

    public Issue getIssueById(long id) {
        return issueRepository.getIssues().stream().filter(it -> it.getId() == id).findFirst().orElse(null);
    }

    // Выдача книги читателю
    public Issue issue(IssueRequest issueRequest) {
        log.info("Лимит выдачи книг на руки составляет {}", maxAllowedBooks);
        if (bookRepository.getBookById(issueRequest.getBookId()) == null) {
            throw new NoSuchElementException("Не найдена книга с id \"" + issueRequest.getBookId() + "\"");
        }

        if (!bookRepository.getBookById(issueRequest.getBookId()).isOnStorage()) {
            throw new NoSuchElementException("Книга с id \"" + issueRequest.getBookId() + "\" на выдаче");
        }

        if (readerRepository.getReaderById(issueRequest.getReaderId()) == null) {
            throw new NoSuchElementException("Не найден читатель с id \"" + issueRequest.getReaderId() + "\"");
        }

        if (readerRepository.getReaderById(issueRequest.getReaderId()).getCountBooks() >= maxAllowedBooks) {
            throw new NoSuchElementException("Превышен максимальный лимит книг на выдачу читателю с id \"" + issueRequest.getReaderId() + "\"");
        }

        Issue issue = new Issue(issueRequest.getBookId(), issueRequest.getReaderId());

        int countBook = readerRepository.getReaderById(issueRequest.getReaderId()).getCountBooks();
        readerRepository.getReaderById(issueRequest.getReaderId()).setCountBooks(countBook + 1);

        bookRepository.getBookById(issueRequest.getBookId()).setOnStorage(false);

        issueRepository.save(issue);
        return issue;
    }

    // Возврат книги в библиотеку
    public Book returnBook(long bookId) {

        if (bookRepository.getBookById(bookId) == null) {
            throw new NoSuchElementException("Не найдена книга с id \"" + bookId + "\"");
        }
        if (bookRepository.getBookById(bookId).isOnStorage()) {
            throw new NoSuchElementException("Книга с id \"" + bookId + "\" не выдавалась");
        }

        Issue currentIssue = issueRepository.getIssues().stream()
                .filter(it -> it.getBookId() == bookId)
                .findFirst()
                .orElse(null);

        long currentReaderId = currentIssue.getReaderId();

        issueRepository.returnBook(bookId);

        int countBook = readerRepository.getReaderById(currentReaderId).getCountBooks();
        readerRepository.getReaderById(currentReaderId).setCountBooks(countBook - 1);

        bookRepository.getBookById(bookId).setOnStorage(true);
        return bookRepository.getBookById(bookId);
    }

//    public List<Book> getListBookByReader(long idReader) {
//        List<Long> idListBookByReader = issueRepository.getListIdBookByReader(idReader);
//        List<Book> bookList = new ArrayList<>();
//        for (Long l : idListBookByReader) {
//            if (!bookRepository.getBookById(l).isOnStorage()){
//                bookList.add(bookRepository.getBookById(l));
//            }
//        }
//        return bookList;
//    }

    public Map<Book, Issue> getListBookByReader(long idReader) {
        List<Long> idListBookByReader = issueRepository.getListIdBookByReader(idReader);
        Map<Book, Issue> bookIssueMap = new HashMap<>();
        for (Long l : idListBookByReader) {
            if (!bookRepository.getBookById(l).isOnStorage()){
                bookIssueMap.put(bookRepository.getBookById(l), issueRepository.getIssuesByIdBook(l));
            }
        }
        return bookIssueMap;
    }
}
