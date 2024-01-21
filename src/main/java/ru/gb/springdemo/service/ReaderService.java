package ru.gb.springdemo.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.repository.ReaderRepository;

import java.util.List;

@Service
@Data
public class ReaderService {
    private ReaderRepository readerRepository;
    @Autowired
    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }
    public List<Reader> getAllReaders(){
        return readerRepository.getAllReaders();
    }

    public Reader getReaderById(long id) {
        return readerRepository.getReaderById(id);
    }
    public void deleteReaderById(long id) {
        readerRepository.deleteReaderById(id);
    }

    public Reader addReader(Reader reader) {
        return readerRepository.addReader(reader);
    }
}
