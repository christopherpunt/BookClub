package bookclub.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class NewBooksController {

    @Autowired
    BookService bookService;

    @GetMapping("/searchGoogleBooks")
    public String showCreateBookForm(){
        return "book/search-google-books";
    }

    @PostMapping("/searchGoogleBooks")
    public ModelAndView searchBookFromTitle(@RequestBody String title, Principal principal){
        List<Book> books = GoogleBookDetailsService.getBooksBasedOnTitle(title);

        ModelAndView modelAndView = new ModelAndView("book/search-google-books.html");
        modelAndView.addObject("books", books);
        return modelAndView;
    }

    @PostMapping(value = "/addBook")
    public ResponseEntity<String> addBook(@RequestBody String bookString, Principal principal) {
        Book book = new Book(bookString);
        boolean success = bookService.newBookForOwner(principal.getName(), book);

        if (success){
            return ResponseEntity.ok("Book added to library successfully");
        }
        return ResponseEntity.badRequest().body("Failed to Add book to library");
    }
}
