package bookclub.controllers;

import bookclub.models.Book;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.UserRepository;
import bookclub.services.BookService;
import bookclub.services.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class BookController {
    @Autowired
    BookService bookService;
    
    @Autowired
    BookRepository bookDao;

    @Autowired
    UserRepository userDao;

    @Autowired
    FriendService friendService;

    @GetMapping("/book_details/{id}")
    public String getBookDetails(@PathVariable Long id, Model model, Principal principal){
        Optional<Book> book = bookDao.findById(id);
        List<User> friends = friendService.findAllFriendsFromUser(principal.getName());

        if(book.isPresent()){
            model.addAttribute("book", book.get());
            model.addAttribute("friends", friends);
            return "book_details";
        }

        return "no book found";
    }

    @GetMapping("/editBookDetails/{id}")
    public String editBookDetails(@PathVariable Long id, Model model){
        Optional<Book> book = bookDao.findById(id);

        if(book.isPresent()){
            model.addAttribute("book", book.get());
            return "edit_book_details";
        }
        return "no book found";
    }

    @PostMapping("/updateBook")
    public String updateBook(@ModelAttribute("book") Book book) {
        boolean updated = bookService.updateAllDetails(book);

        if (updated){
            return "redirect:/book_details/" + book.getId();
        }
        return "redirect:/home";
    }

    @PostMapping("/deleteBook/{id}")
    public RedirectView removeBook(@PathVariable Long id, Model model){
        bookDao.deleteById(id);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/home");
        return redirectView;
    }
}
