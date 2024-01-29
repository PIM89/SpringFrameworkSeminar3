package ru.gb.springdemo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.service.IssuerService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ui")
public class IssuesController {
    private IssuerService issuerService;

    @Autowired
    public IssuesController(IssuerService issuerService) {
        this.issuerService = issuerService;
    }

    @GetMapping("/issues")
    public String getIssues(Model model) {
        List<Issue> issues = issuerService.findAllByIssueTimestampNotNull();
        model.addAttribute("issues", issues);
        return "issue";
    }


    @GetMapping("/reader/{id}")
    public String getInfoByReader(@PathVariable long id, Model model) {
        model.addAttribute("issues", issuerService.getListBookByReader(id));
        model.addAttribute("reader", issuerService.getReaderRepository().findById(id));
        return "readerInfo";
    }
}
