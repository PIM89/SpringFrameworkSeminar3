package ru.gb.springdemo.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.service.IssuerService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/issue")
public class IssuerController {
    private IssuerService issuerService;

    @Autowired
    public IssuerController(IssuerService issuerService) {
        this.issuerService = issuerService;
    }

    @PostMapping
    public ResponseEntity<Issue> issueBook(@RequestBody IssueRequest request) {
        log.info("Получен запрос на выдачу: readerId = {}, bookId = {}", request.getReaderId(), request.getBookId());

        final Issue issue;

        try {
            issue = issuerService.issue(request);
        } catch (NoSuchElementException e) {
            log.info("Кгига(id{}) не выдана читателю(id={})", request.getBookId(), request.getReaderId());
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(issue);
    }

    @GetMapping()
    public List<Issue> getAllIssue() {
        return issuerService.getAllIssue();
    }

    @GetMapping("/{id}")
    public Issue getIssueById(@PathVariable long id) {
        return issuerService.getIssueById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> returnBook (@PathVariable long id) {
        log.info("Получен запрос на возврат книги с id={}", id);
        Book book = issuerService.getBookRepository().getBookById(id);
        try {
            book = issuerService.returnBook(id);
        } catch (NoSuchElementException e) {
            log.info("Ошибка при возврате книги с id={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(book);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(book);
    }
}
