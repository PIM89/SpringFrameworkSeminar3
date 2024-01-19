package ru.gb.springdemo.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
public class Book {
    private static long sequence = 1L;
    private final long id;
    private String name;
    private boolean onStorage;

    public Book(String name) {
        this.name = name;
        this.onStorage = true;
        this.id = sequence++;
    }
}
