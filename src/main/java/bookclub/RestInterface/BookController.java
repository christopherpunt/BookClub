package bookclub.RestInterface;
import bookclub.book.Book;
import bookclub.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    BookService bookService;

    @RequestMapping(value= "/book", method= RequestMethod.POST)
    public Book createBook(@RequestBody Book book){
        return bookService.createBook(book);
    }

    @RequestMapping(value="/books", method = RequestMethod.GET)
    public List<Book> readBooks(){
        return bookService.getBooks();
    }

}
