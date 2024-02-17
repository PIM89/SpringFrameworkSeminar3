package ru.gb.springdemo.api;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.repository.IssuerRepository;
import ru.gb.springdemo.repository.ReaderRepository;
import ru.gb.springdemo.service.IssuerService;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class IssuerRestControllerTest {
    @Autowired
    IssuerRepository issuerRepository;
    @Autowired
    IssuerService issuerService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    ReaderRepository readerRepository;

    @Autowired
    WebTestClient webTestClient;

    void cleanRepo() {
        issuerRepository.deleteAll();
        bookRepository.deleteAll();
        readerRepository.deleteAll();
    }

    @Test
    void testIssuanceSuccess() {
        Book book = new Book("Чистый код");
        bookRepository.save(book);

        Reader reader = new Reader("Илья");
        readerRepository.save(reader);

        IssueRequest issueRequest = new IssueRequest();
        issueRequest.setBookId(book.getId());
        issueRequest.setReaderId(reader.getId());

        Issue responseBody = webTestClient.post()
                .uri("/issue")
                .bodyValue(issueRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Issue.class)
                .returnResult().getResponseBody();

        assert responseBody != null;

        Assertions.assertEquals(issuerRepository.count(), 1);

        Assertions.assertEquals(responseBody.getBook().getName(), book.getName());
        Assertions.assertEquals(responseBody.getBook().getOnStorage(), false);

        Assertions.assertEquals(responseBody.getReader().getName(), reader.getName());
        Assertions.assertEquals(responseBody.getReader().getCountBooks(), 1);
        cleanRepo();
    }

    @Test
    void testIssuanceExceptions() {
        webTestClient.post()
                .uri("/issue")
                .bodyValue(new IssueRequest())
                .exchange()
                .expectStatus().isNotFound();
        cleanRepo();
    }

    @Test
    void testGetAllIssueSuccess() {
        List<Issue> expected = issuerRepository.saveAll(List.of(
                new Issue(new Book("Война и мир"), new Reader("Илья")),
                new Issue(new Book("Чистый код"), new Reader("Валерий")),
                new Issue(new Book("Паттерны программирования"), new Reader("Иван"))
        ));

        List<Issue> responseBody = webTestClient.get()
                .uri("/issue")
                .exchange()
                .expectStatus().isFound()
                .expectBody(new ParameterizedTypeReference<List<Issue>>() {
                })
                .returnResult().getResponseBody();

        assert responseBody != null;
        Assertions.assertEquals(expected.size(), responseBody.size());
        for (Issue issue : responseBody) {
            boolean b = expected.stream()
                    .filter(it -> it.getReader().getId().equals(issue.getReader().getId()))
                    .filter(it -> it.getReader().getName().equals(issue.getReader().getName()))
                    .filter(it -> it.getReader().getCountBooks().equals(issue.getReader().getCountBooks()))
                    .filter(it -> it.getBook().getId().equals(issue.getBook().getId()))
                    .filter(it -> it.getBook().getName().equals(issue.getBook().getName()))
                    .filter(it -> it.getBook().getOnStorage().equals(issue.getBook().getOnStorage()))
                    .anyMatch(it -> it.getIssueTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            .equals(issue.getIssueTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            Assertions.assertTrue(b);
        }
        cleanRepo();
    }

    @Test
    void testGetAllIssueNoContent() {
        webTestClient.get()
                .uri("/issue")
                .exchange()
                .expectStatus().isNoContent();
        cleanRepo();
    }

    @Test
    void testGetIssueByIdSuccess() {
        Issue expected = issuerRepository.save(new Issue(new Book("Чистый код"), new Reader("Илья")));

        Issue responseBody = webTestClient.get()
                .uri("/issue/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Issue.class)
                .returnResult().getResponseBody();

        assert responseBody != null;
        Assertions.assertEquals(expected.getReader().getId(), responseBody.getReader().getId());
        Assertions.assertEquals(expected.getReader().getName(), responseBody.getReader().getName());
        Assertions.assertEquals(expected.getReader().getCountBooks(), responseBody.getReader().getCountBooks());
        Assertions.assertEquals(expected.getBook().getId(), responseBody.getBook().getId());
        Assertions.assertEquals(expected.getBook().getOnStorage(), responseBody.getBook().getOnStorage());
        cleanRepo();
    }

    @Test
    void testGetIssueByIdSuccessNoFound() {
        webTestClient.get()
                .uri("/issue/-1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testReturnBookSuccess() {
        Book book = new Book("Чистый код");
        bookRepository.save(book);

        Reader reader = new Reader("Илья");
        readerRepository.save(reader);

        IssueRequest issueRequest = new IssueRequest();
        issueRequest.setBookId(book.getId());
        issueRequest.setReaderId(reader.getId());

        issuerService.issuance(issueRequest);

        Book responseBody = webTestClient.put()
                .uri("/issue/" + book.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .returnResult().getResponseBody();
        assert responseBody != null;
        Assertions.assertEquals(book.getName(), responseBody.getName());
        Assertions.assertEquals(book.getOnStorage(), responseBody.getOnStorage());
        cleanRepo();
    }

    @Test
    void testReturnBookException() {
        webTestClient.put()
                .uri("/issue/-1")
                .exchange()
                .expectStatus().isNotFound();
        cleanRepo();
    }
}