package ru.gb.springdemo.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
@Schema(name = "Выдача книг")
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "Идентификатор")
    private Long id;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    @Schema(name = "Идентификатор книги")
    private Book book;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "reader_id", referencedColumnName = "id")
    @Schema(name = "Идентификатор читателя")
    private Reader reader;

    @Column(name = "issue_time")
    @Schema(name = "да и время выдачи книги")
    private LocalDateTime issueTimestamp = LocalDateTime.now();

    @Column(name = "return_time")
    @Schema(name = "Дата и время возвращения книги")
    private LocalDateTime returnTimestamp = null;

    public Issue(Book book, Reader reader) {
        this.book = book;
        this.reader = reader;
    }
}
