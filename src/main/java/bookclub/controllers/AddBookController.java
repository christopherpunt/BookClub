package bookclub.controllers;

import bookclub.models.Book;
import bookclub.services.BookService;
import bookclub.services.GoogleBookDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AddBookController {

    @Autowired
    BookService bookService;

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
    public String AddBook(@RequestParam String bookItem){
        Book book = GoogleBookDetailsService.getBookDetailsFromIsbn(bookItem);

        bookService.createBook(book);
        return "redirect:/";
    }
}
