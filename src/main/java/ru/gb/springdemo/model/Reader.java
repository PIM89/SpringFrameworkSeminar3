package ru.gb.springdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@JsonIgnoreProperties({"issue"})
@Schema(name = "Читатель")
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "Идентификатор")
    private Long id;

    @Column(name = "name")
    @Schema(name = "Имя")
    private String name;
    @Column(name = "count_books")
    @Schema(name = "Количество книг на 'руках'")
    private Integer countBooks = 0;
    @OneToOne(mappedBy = "reader")
    private Issue issue;

    public Reader(String name) {
        this.name = name;
    }
}
