package ru.gb.springdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "books")
@NoArgsConstructor
@Data
@JsonIgnoreProperties({"issue"})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "storage")
    private Boolean onStorage = true;
    @OneToOne(mappedBy = "book")
    private Issue issue;

    public Book(String name) {
        this.name = name;
    }
}
