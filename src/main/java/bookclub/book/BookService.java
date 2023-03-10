package bookclub.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    BookRepository bookDao;

    public bookclub.book.Book createBook(Book book){
        return bookDao.save(book);
    }

    public List<bookclub.book.Book> getBooks(){
        return bookDao.findAll();
    }

    public static Book getBookDetails(String isbn){
        return GoogleBookDetails.getBookDetailsFromIsbn(isbn);
    }

    public static List<Book> getBooksFromTitle(String title){
        return GoogleBookDetails.getBooksBasedOnTitle(title);
    }
}
