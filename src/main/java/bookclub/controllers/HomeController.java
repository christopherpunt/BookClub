package bookclub.controllers;

import bookclub.models.User;
import bookclub.repositories.UserRepository;
import bookclub.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Optional;

@RestController
public class HomeController {

    @Autowired
    BookService bookService;

    @Autowired
    UserRepository userDao;

    @GetMapping("/home")
    public ModelAndView index(Principal principal){
        Optional<User> userOptional = userDao.findByEmail(principal.getName());
        ModelAndView modelAndView = new ModelAndView("home.html");
        modelAndView.addObject("books", bookService.getAllBooksForUser(principal.getName()));
        modelAndView.addObject("fullName", userOptional.get().getFullName());
        return modelAndView;
    }
}
