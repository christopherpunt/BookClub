package bookclub.controllers;

import bookclub.models.Book;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.UserRepository;
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
    UserRepository userDao;

    @Autowired
    BookService bookService;
    
    @Autowired
    BookRepository bookDao;
    
    @RequestMapping(value= "/bookisbn", method= RequestMethod.POST)
    public Book createBookFromIsbn(@RequestBody String isbn){
        return bookService.createBook(BookService.getBookDetails(isbn));
    }

    @RequestMapping(value="/books", method = RequestMethod.GET)
    public List<Book> readBooks(){
        return bookService.getBooks();
    }

    @GetMapping("/book")
    public String showCreateBookForm(){
        return "search-books";
    }

    @PostMapping("/book")
    public ModelAndView searchBookFromTitle(@RequestBody String title){
        var books = GoogleBookDetailsService.getBooksBasedOnTitle(title);

        ModelAndView modelAndView = new ModelAndView("search-books.html");
        modelAndView.addObject("books", books);
        return modelAndView;
    }

    @PostMapping(value = "/addBook", consumes = "multipart/form-data")
    public ResponseEntity<String> addBook(@RequestParam String bookItem, Principal principal) {
        Optional<User> user = userDao.findByEmail(principal.getName());
        if (user.isPresent()) {
            Book book = GoogleBookDetailsService.getBookDetailsFromIsbn(bookItem);
            assert book != null;
            book.setUser(user.get());
            book.setDescription("");
            bookService.createBook(book);
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

    @PostMapping("/deleteBook/{id}")
    public RedirectView removeBook(@PathVariable int id, Model model){
        bookService.deleteBook(id);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/home");
        return redirectView;
    }
}
