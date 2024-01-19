package ru.gb.springdemo.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.model.Reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Repository
public class ReaderRepository {
    private List<Reader> readers;

    public ReaderRepository() {
        this.readers = new ArrayList<>();
    }

    @PostConstruct
    public void generateData() {
        readers.addAll(List.of(
                new ru.gb.springdemo.model.Reader("Илья")
        ));
    }

    public List<Reader> getAllReaders() {
        return List.copyOf(readers);
    }

    public Reader getReaderById(long id) {
        return readers.stream().filter(it -> Objects.equals(it.getId(), id))
                .findFirst()
                .orElse(null);
    }

    public void deleteReaderById(long id) {
        for (Reader reader : readers) {
            if (reader.getId() == id) {
                readers.remove(reader);
                break;
            }
        }
    }

    public void addReader(Reader reader) {
        readers.add(reader);
    }
}
