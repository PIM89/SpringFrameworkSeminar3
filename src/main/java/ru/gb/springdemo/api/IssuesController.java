package ru.gb.springdemo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.springdemo.service.IssuerService;

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
        model.addAttribute("issues", issuerService.getIssueRepository().getIssues());
        model.addAttribute("books", issuerService.getBookRepository());
        model.addAttribute("readers", issuerService.getReaderRepository());
        return "issue";
    }


    @GetMapping("/reader/{id}")
    public String getInfoByReader(@PathVariable long id, Model model) {
        model.addAttribute("reader", issuerService.getReaderRepository().getReaderById(id));
        model.addAttribute("books", issuerService.getListBookByReader(id));
        return "readerInfo";
    }
}
