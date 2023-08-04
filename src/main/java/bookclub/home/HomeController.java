package bookclub.home;

import bookclub.book.BookService;
import bookclub.user.User;
import bookclub.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    BookService bookService;

    @Autowired
    UserRepository userRepo;

    @GetMapping("/")
    public String index(Model model, Principal principal){
        Optional<User> userOptional = userRepo.findByEmail(principal.getName());
        if (userOptional.isEmpty()){
            return "redirect:/login";
        }
        model.addAttribute("books", bookService.getAllBooksForUser(principal.getName()));
        model.addAttribute("fullName", userOptional.get().getFullName());
        return "index";
    }
}
