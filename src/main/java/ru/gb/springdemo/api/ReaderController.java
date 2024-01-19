package ru.gb.springdemo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.repository.ReaderRepository;

import java.util.List;

@RestController
@RequestMapping("/reader")
public class ReaderController {
    private ReaderRepository readerRepository;

    @Autowired
    public ReaderController(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    /**
     * Метод возвращает список всех читателей
     * @return
     */
    @GetMapping
    public List<Reader> getReaders() {
        return readerRepository.getAllReaders();
    }

    /**
     * Метод возвращает информацию о читателе с заданным id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Reader getReaderById(@PathVariable long id) {
        return readerRepository.getReaderById(id);
    }

    /**
     * Метод удаляет читателя с заданным id и возвращает список оставшихся в репозитории читателей
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public List<Reader> deleteReaderById(@PathVariable long id) {
        readerRepository.deleteReaderById(id);
        return readerRepository.getAllReaders();
    }

    /**
     * Метод добавляет читателя в репозиторий и возвращает его
     * @param
     * @return
     */
    @PostMapping("/add/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    public Reader addReader(@PathVariable String name) {
        readerRepository.addReader(new Reader(name));
        return readerRepository.getAllReaders().getLast();
    }
}
