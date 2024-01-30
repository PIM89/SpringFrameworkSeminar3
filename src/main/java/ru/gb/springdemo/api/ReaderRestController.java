package ru.gb.springdemo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.service.ReaderService;

import java.util.List;

@RestController
@RequestMapping("/reader")
@Tag(name = "Reader")
public class ReaderRestController {
    private ReaderService readerService;

    @Autowired
    public ReaderRestController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    @Operation(summary = "Get all readers", description = "Возвращает список всех имеющихся читателей в БД")
    public List<Reader> getReaders() {
        return readerService.findAll();
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get reader by ID", description = "Возвращает информацию о читателе с заданным идентификатором")
    public Reader getReaderById(@PathVariable long id) {
        return readerService.findById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete reader by ID", description = "Удаляет читателя из БД с заданным идентификатором")
    public List<Reader> deleteReaderById(@PathVariable long id) {
        readerService.deleteById(id);
        return readerService.findAll();
    }

    @PostMapping("/add/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add reader", description = "Добавляет нового читателя в БД")
    public Reader addReader(@PathVariable String name) {
        Reader reader = new Reader(name);
        readerService.saveReader(reader);
        return reader;
    }
}
