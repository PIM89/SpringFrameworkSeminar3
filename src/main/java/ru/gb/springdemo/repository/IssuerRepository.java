package ru.gb.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.springdemo.model.Issue;

import java.util.List;
import java.util.Optional;

public interface IssuerRepository extends JpaRepository<Issue, Long> {
    Optional<Issue> findIssueByBookId(Long id);
    List<Issue> findAllIssueByReaderIdAndIssueTimestampNotNull(Long id);
    List<Issue> findAllByIssueTimestampNotNull();
}
