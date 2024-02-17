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
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.repository.ReaderRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class ReaderRestControllerTest {
    @Autowired
    ReaderRepository readerRepository;
    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void clearReaderRepo() {
        readerRepository.deleteAll();
    }

    @Test
    void testGetAllReadersSuccess() {
        List<Reader> expected = readerRepository.saveAll(List.of(
                new Reader("Илья"),
                new Reader("Василий"),
                new Reader("Игорь")
        ));

        List<Reader> responseBody = webTestClient.get()
                .uri("/reader")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<Reader>>() {
                })
                .returnResult().getResponseBody();

        assert responseBody != null;
        Assertions.assertEquals(expected.size(), responseBody.size());

        for (Reader reader : responseBody) {
            boolean b = expected.stream()
                    .filter(it -> it.getId().equals(reader.getId()))
                    .filter(it -> it.getName().equals(reader.getName()))
                    .anyMatch(it -> it.getCountBooks().equals(reader.getCountBooks()));
            Assertions.assertTrue(b);
        }
    }

    @Test
    void testGetAllReadersNoContent() {
        webTestClient.get()
                .uri("/reader")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testGetReaderById() {
        Reader reader = readerRepository.save(new Reader("Илья"));

        Reader result = webTestClient.get()
                .uri("/reader/" + reader.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Reader.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), reader.getId());
        Assertions.assertEquals(result.getName(), reader.getName());
        Assertions.assertEquals(result.getIssue(), reader.getIssue());
        Assertions.assertEquals(result.getCountBooks(), reader.getCountBooks());
    }

    @Test
    void testGetReaderByIdNotFound() {
        webTestClient.get()
                .uri("/reader/-1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteReaderById() {
        Reader reader = readerRepository.save(new Reader("Илья"));

        webTestClient.delete()
                .uri("/reader/" + reader.getId())
                .exchange()
                .expectStatus().isOk();

        Assertions.assertFalse(readerRepository.existsById(reader.getId()));
    }

    @Test
    void testDeleteReaderByIdNotFound() {
        webTestClient.delete()
                .uri("/reader/-1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testAddReader() {
        String readerName = "Илья";
        Reader responseBody = webTestClient.post()
                .uri("/book/add/" + readerName)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Reader.class)
                .returnResult().getResponseBody();

        assert responseBody != null;
        Assertions.assertEquals(responseBody.getName(), readerName);
    }
}