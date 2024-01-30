package ru.gb.springdemo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Issue")
public class IssuerRestController {
    private IssuerService issuerService;

    @Autowired
    public IssuerRestController(IssuerService issuerService) {
        this.issuerService = issuerService;
    }

    @PostMapping
    @Operation(summary = "Handing out a book to a reader", description = "Операция по выдаче книги на руки читателю")
    public ResponseEntity<Issue> issuance(@RequestBody IssueRequest request) {
        log.info("Получен запрос на выдачу: readerId = {}, bookId = {}", request.getReaderId(), request.getBookId());
        final Issue issue;
        try {
            issue = issuerService.issuance(request);
        } catch (Exception e) {
            log.info("{}", e.getMessage());
            log.info("Кгига (id={}) не выдана читателю (id={})", request.getBookId(), request.getReaderId());
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(issue);
    }

    @GetMapping()
    @Operation(summary = "Get all issue", description = "Возвращает список всех имеющихся операций по выдаче книг читателям")
    public ResponseEntity<List<Issue>> getAllIssue() {
        try {
            List<Issue> issues = issuerService.findAll();
            return ResponseEntity.status(HttpStatus.FOUND).body(issues);
        } catch (Exception e){
            log.info("{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get issue by ID", description = "Возвращает информацию об операции (выдача книги) по идентификатору")
    public Issue getIssueById(@PathVariable long id) {
        return issuerService.findIssueById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Return book", description = "Операция по возврату книги от читателя в библиотеку")
    public ResponseEntity<Book> returnBook (@PathVariable long id) {
        log.info("Получен запрос на возврат книги с id={}", id);
        try {
            return ResponseEntity.ok(issuerService.returnBook(id));
        } catch (Exception e) {
            log.info("Ошибка при возврате книги с id={}", id);
            log.info("{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
