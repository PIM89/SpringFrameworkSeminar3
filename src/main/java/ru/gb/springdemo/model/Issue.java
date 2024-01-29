package ru.gb.springdemo.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "reader_id", referencedColumnName = "id")
    private Reader reader;

    @Column(name = "issue_time")
    private LocalDateTime issueTimestamp = LocalDateTime.now();

    @Column(name = "return_time")
    private LocalDateTime returnTimestamp = null;

    public Issue(Book book, Reader reader) {
        this.book = book;
        this.reader = reader;
    }
}
