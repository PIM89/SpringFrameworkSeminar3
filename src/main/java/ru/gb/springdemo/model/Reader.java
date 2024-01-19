package ru.gb.springdemo.model;

import lombok.Data;

@Data
public class Reader {
    private static long sequence = 1L;
    private final long id;
    private String name;
    private int countBooks;

    public Reader(String name) {
        this.name = name;
        this.id = sequence++;
    }
}
