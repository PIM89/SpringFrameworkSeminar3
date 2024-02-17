package ru.gb.springdemo.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.repository.BookRepository;

import java.util.List;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class BookRestControllerTest {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    BookRepository bookRepository;
    @Test
    void testGetAllBooksSuccess() {
        List<Book> expected = bookRepository.saveAll(List.of(
                new Book("Война и мир"),
                new Book("Чистый код"),
                new Book("Паттерны програмирования")
        ));

        List<Book> responseBody = webTestClient.get()
                .uri("/book")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<Book>>() {
                })
                .returnResult()
                .getResponseBody();

        assert responseBody != null;
        Assertions.assertEquals(expected.size(), responseBody.size());

        for (Book book : responseBody) {
            boolean b = expected.stream()
                    .filter(it -> it.getId().equals(book.getId()))
                    .filter(it -> it.getName().equals(book.getName()))
                    .anyMatch(it -> it.getOnStorage().equals(book.getOnStorage()));
            Assertions.assertTrue(b);
        }
        bookRepository.deleteAll();
    }

    @Test
    void testGetAllBooksNoContent() {
        webTestClient.get()
                .uri("/book")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testGetBookById() {
        Book book = bookRepository.save(new Book("Война и мир"));

        Book result = webTestClient.get()
                .uri("/book/" + book.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), book.getId());
        Assertions.assertEquals(result.getName(), book.getName());
        Assertions.assertEquals(result.getIssue(), book.getIssue());
        Assertions.assertEquals(result.getOnStorage(), book.getOnStorage());
        bookRepository.deleteAll();
    }

    @Test
    void testGetBookByIdNotFound() {
        webTestClient.get()
                .uri("/book/-1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteBookById() {
        Book book = bookRepository.save(new Book("Война и мир"));

        webTestClient.delete()
                .uri("/book/" + book.getId())
                .exchange()
                .expectStatus().isOk();

        Assertions.assertFalse(bookRepository.existsById(book.getId()));
        bookRepository.deleteAll();
    }

    @Test
    void testDeleteBookByIdNotFound() {
        webTestClient.delete()
                .uri("/book/-1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testAddBook() {
        String bookName = "Чистый код";
        Book responseBody = webTestClient.post()
                .uri("/book/add/" + bookName)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class)
                .returnResult().getResponseBody();

        assert responseBody != null;
        Assertions.assertEquals(responseBody.getName(), bookName);
        bookRepository.deleteAll();
    }
}