package bookclub.controllers;

import bookclub.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

    @Autowired
    BookService bookService;

    @GetMapping("/home")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("index.html");
        modelAndView.addObject("books", bookService.getBooks());
        return modelAndView;
    }
}
