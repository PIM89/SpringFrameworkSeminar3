package ru.gb.springdemo.repository;

import org.springframework.stereotype.Repository;
import ru.gb.springdemo.model.Issue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class IssueRepository {

    private List<Issue> issues;

    public IssueRepository() {
        this.issues = new ArrayList<>();
    }

    public void save(Issue issue) {
        issues.add(issue);
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public Issue getIssuesByIdBook(long id){
        return issues.stream()
                .filter(it -> it.getBookId() == id && it.getReturnTimestamp() == null)
                .findFirst()
                .orElse(null);
    }

    public void returnBook(long id) {
        for (Issue issue : issues) {
            if (issue.getId() == id) {
                issue.setReturnTimestamp(LocalDateTime.now());
                break;
            }
        }
    }

    public List<Long> getListIdBookByReader(long id){
        List<Long> idBooks = new ArrayList<>();
        for (Issue issue : issues) {
            if (issue.getReaderId() == id) {
                idBooks.add(issue.getBookId());
            }
        }
        return idBooks;
    }


}
