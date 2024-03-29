package bookclub.book;

import bookclub.friend.FriendService;
import bookclub.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class BookController {
    @Autowired
    BookService bookService;
    
    @Autowired
    BookRepository bookRepo;

    @Autowired
    FriendService friendService;

    @GetMapping("/book_details/{id}")
    public String getBookDetails(@PathVariable Long id, Model model, Principal principal){
        Optional<Book> book = bookRepo.findById(id);
        List<User> friends = friendService.findAllFriendsFromUser(principal.getName());

        if(book.isPresent()){
            model.addAttribute("book", book.get());
            model.addAttribute("friends", friends);
            return "book/book_details";
        }

        return "no book found";
    }

    @GetMapping("/editBookDetails/{id}")
    public String editBookDetails(@PathVariable Long id, Model model){
        Optional<Book> book = bookRepo.findById(id);

        if(book.isPresent()){
            model.addAttribute("book", book.get());
            return "book/edit_book_details";
        }
        return "no book found";
    }

    @PostMapping("/updateBook")
    public String updateBook(@ModelAttribute("book") Book book) {
        boolean updated = bookService.updateAllDetails(book);

        if (updated){
            return "redirect:/book/book_details/" + book.getId();
        }
        return "redirect:/";
    }

    @PostMapping("/deleteBook/{id}")
    public String removeBook(@PathVariable Long id, Model model){
        bookRepo.deleteById(id);
        return "redirect:/";
    }
}
