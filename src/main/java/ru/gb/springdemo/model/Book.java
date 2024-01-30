package ru.gb.springdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "books")
@NoArgsConstructor
@Data
@JsonIgnoreProperties({"issue"})
@Schema(name = "Книга")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "Идентификатор")
    private Long id;

    @Column(name = "name")
    @Schema(name = "Название книги")
    private String name;

    @Column(name = "storage")
    @Schema(name = "На хранении")
    private Boolean onStorage = true;
    @OneToOne(mappedBy = "book")
    private Issue issue;

    public Book(String name) {
        this.name = name;
    }
}
