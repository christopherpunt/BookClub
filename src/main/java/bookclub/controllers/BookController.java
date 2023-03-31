package bookclub.controllers;

import bookclub.models.Book;
import bookclub.repositories.BookRepository;
import bookclub.services.BookService;
import bookclub.services.GoogleBookDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
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

    @GetMapping("/searchBooks")
    public String showCreateBookForm(){
        return "search-books";
    }

    @PostMapping("/searchBooks")
    public ModelAndView searchBookFromTitle(@RequestBody String title){
        List<Book> books = GoogleBookDetailsService.getBooksBasedOnTitle(title);

        ModelAndView modelAndView = new ModelAndView("search-books.html");
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

    @GetMapping("/book_details/{id}")
    public String getBookDetails(@PathVariable int id, Model model){
        Optional<Book> book = bookDao.findById(id);

        if(book.isPresent()){
            model.addAttribute("book", book.get());
            return "book_details";
        }
        return "no book found";
    }

    @GetMapping("/editBookDetails/{id}")
    public String editBookDetails(@PathVariable int id, Model model){
        Optional<Book> book = bookDao.findById(id);

        if(book.isPresent()){
            model.addAttribute("book", book.get());
            return "edit_book_details";
        }
        return "no book found";
    }

    @PostMapping("/deleteBook/{id}")
    public RedirectView removeBook(@PathVariable int id, Model model){
        bookService.deleteBook(id);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/home");
        return redirectView;
    }

    @PostMapping("/updateBook")
    public String updateBook(@ModelAttribute("book") Book book) {
        boolean updated = bookService.updateAllDetails(book);

        if (updated){
            return "redirect:/book_details/" + book.getId();
        }
        return "redirect:/home";
    }
}
