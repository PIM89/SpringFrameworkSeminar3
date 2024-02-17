package ru.gb.springdemo.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.repository.ReaderRepository;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class ReaderService {
    private final ReaderRepository readerRepository;

    @Autowired
    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    public List<Reader> findAll() {
        return readerRepository.findAll();
    }

    public Optional<Reader> findById(Long id) {
        return readerRepository.findById(id);
    }

    public void deleteById(Long id) {
        readerRepository.deleteById(id);
    }

    public Reader saveReader(Reader reader) {
        return readerRepository.save(reader);
    }
}
