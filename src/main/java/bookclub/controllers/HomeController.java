package bookclub.controllers;

import bookclub.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
//@RequestMapping("/")
public class HomeController {

    @Autowired
    BookService bookService;

    @GetMapping("/")
    public ModelAndView index(){
//        return "welcome to book club test";

        ModelAndView modelAndView = new ModelAndView("index.html");
        modelAndView.addObject("books", bookService.getBooks());
        return modelAndView;
    }

}
