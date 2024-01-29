package ru.gb.springdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@JsonIgnoreProperties({"issue"})
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "count_books")
    private Integer countBooks = 0;
    @OneToOne(mappedBy = "reader")
    private Issue issue;

    public Reader(String name) {
        this.name = name;
    }
}
