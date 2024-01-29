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
    private final ReaderRepository readerRepository;

    @Autowired
    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    public List<Reader> findAll(){
        return readerRepository.findAll();
    }

    public Reader findById(Long id){
        return readerRepository.findById(id).get();
    }

    public void deleteById (Long id){
        readerRepository.deleteById(id);
    }
    public Reader saveReader(Reader reader){
        readerRepository.save(reader);
        return reader;
    }
}
