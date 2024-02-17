package ru.gb.springdemo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.service.ReaderService;

import java.util.List;

@RestController
@RequestMapping("/reader")
@Tag(name = "Reader")
@ComponentScan
public class ReaderRestController {
    private ReaderService readerService;

    @Autowired
    public ReaderRestController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    @Operation(summary = "Get all readers", description = "Возвращает список всех имеющихся читателей в БД")
    public ResponseEntity<List<Reader>> getReaders() {
        List<Reader> readers = readerService.findAll();
        if (!readers.isEmpty()) {
            return ResponseEntity.ok(readers);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get reader by ID", description = "Возвращает информацию о читателе с заданным идентификатором")
    public ResponseEntity<Reader> getReaderById(@PathVariable long id) {
        return readerService.findById(id)
                .map(it -> ResponseEntity.ok(it))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete reader by ID", description = "Удаляет читателя из БД с заданным идентификатором")
    public ResponseEntity<Reader> deleteReaderById(@PathVariable long id) {
        if (readerService.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            readerService.deleteById(id);
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/add/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add reader", description = "Добавляет нового читателя в БД")
    public ResponseEntity<Reader> addReader(@PathVariable String name) {
        Reader reader = new Reader(name);
        try {
            readerService.saveReader(reader);
            return ResponseEntity.status(HttpStatus.CREATED).body(reader);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }
}
