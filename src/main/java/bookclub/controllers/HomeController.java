package bookclub.controllers;

import bookclub.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
public class HomeController {

    @Autowired
    BookService bookService;

    @GetMapping("/home")
    public ModelAndView index(Principal principal){
        ModelAndView modelAndView = new ModelAndView("home.html");
        modelAndView.addObject("books", bookService.getAllBooksForUser(principal.getName()));
        modelAndView.addObject("username", principal.getName());
        return modelAndView;
    }
}
