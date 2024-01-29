package ru.gb.springdemo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.service.ReaderService;

import java.util.List;

@RestController
@RequestMapping("/reader")
public class ReaderRestController {
    private ReaderService readerService;

    @Autowired
    public ReaderRestController(ReaderService readerService) {
        this.readerService = readerService;
    }

    /**
     * Метод возвращает список всех читателей
     * @return
     */
    @GetMapping
    public List<Reader> getReaders() {
        return readerService.findAll();
    }

    /**
     * Метод возвращает информацию о читателе с заданным id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Reader getReaderById(@PathVariable long id) {
        return readerService.findById(id);
    }

    /**
     * Метод удаляет читателя с заданным id и возвращает список оставшихся в репозитории читателей
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public List<Reader> deleteReaderById(@PathVariable long id) {
        readerService.deleteById(id);
        return readerService.findAll();
    }

    /**
     * Метод добавляет читателя в репозиторий и возвращает его
     * @param
     * @return
     */
    @PostMapping("/add/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    public Reader addReader(@PathVariable String name) {
        Reader reader = new Reader(name);
        readerService.saveReader(reader);
        return reader;
    }
}
