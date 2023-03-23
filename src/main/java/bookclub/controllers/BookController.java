package bookclub.controllers;

import bookclub.models.Book;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.UserRepository;
import bookclub.services.BookService;
import bookclub.services.GoogleBookDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

//    @RequestMapping(value= "/book", method= RequestMethod.POST)
//    public Book createBook(@RequestBody Book book){
//        return bookService.createBook(book);
//    }

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
    public String AddBook(@RequestParam String bookItem, Principal principal){
        Optional<User> user = userDao.findByEmail(principal.getName());
        if (user.isPresent()){
            Book book = GoogleBookDetailsService.getBookDetailsFromIsbn(bookItem);

            //TODO:replace with optional
            assert book != null;
            book.setUser(user.get());

            //TODO: book wont save if the description is too long, so set it to blank for now
            book.setDescription("");
            bookService.createBook(book);
        }

        return "index";
    }

    @GetMapping("/book_details")
    public String getBookDetails(@RequestParam int Id){
        Optional<Book> book = bookDao.findById(Id);

        if(book.isPresent()){
            return "book found: " + book.get().getTitle();
        }
        return "no book found";
    }
}
