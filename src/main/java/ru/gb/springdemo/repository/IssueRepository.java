package ru.gb.springdemo.repository;

import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.gb.springdemo.model.Issue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public void returnBook(long id) {
        for (Issue issue : issues) {
            if (issue.getId() == id) {
                issue.setReturnTimestamp(LocalDateTime.now());
                break;
            }
        }
    }
}
