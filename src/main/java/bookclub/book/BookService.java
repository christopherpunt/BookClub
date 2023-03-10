package bookclub.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    BookRepository bookDao;

    public Book createBook(Book book){
        return bookDao.save(book);
    }

    public List<Book> getBooks(){
        return bookDao.findAll();
    }

    public static Book getBookDetails(String isbn){
        return GoogleBookDetails.getBookDetailsFromIsbn(isbn);
    }

    public static List<Book> getBooksFromTitle(String title){
        return GoogleBookDetails.getBooksBasedOnTitle(title);
    }
}
