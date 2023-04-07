package bookclub.controllers;

import bookclub.models.Book;
import bookclub.services.BookService;
import bookclub.services.GoogleBookDetailsService;
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
        return "search-google-books";
    }

    @PostMapping("/searchGoogleBooks")
    public ModelAndView searchBookFromTitle(@RequestBody String title, Principal principal){
        List<Book> books = GoogleBookDetailsService.getBooksBasedOnTitle(title);

        ModelAndView modelAndView = new ModelAndView("search-google-books.html");
        modelAndView.addObject("books", books);
        return modelAndView;
    }

    @PostMapping(value = "/addBook", consumes = "application/json")
    public ResponseEntity<String> addBook(@RequestBody Book book, Principal principal) {
        boolean success = bookService.addBookForUser(principal.getName(), book);

        if (success){
            return ResponseEntity.ok("Book added to library successfully");
        }
        return ResponseEntity.badRequest().body("Failed to Add book to library");
    }
}
