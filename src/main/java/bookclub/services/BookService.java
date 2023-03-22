package bookclub.services;

import bookclub.models.Book;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    BookRepository bookDao;

    @Autowired
    UserRepository userRepo;

    public Book createBook(Book book){
        return bookDao.save(book);
    }

    public List<Book> getBooks(){
        return bookDao.findAll();
    }

    public List<Book> getAllBooksForUser(String username){
        Optional<User> user = userRepo.findByEmail(username);
        List<Book> books = new ArrayList<Book>();

        user.ifPresent(u -> books.addAll(bookDao.findAllBooksForUser(u.getId())));

        return books;
    }

    public static Book getBookDetails(String isbn){
        return GoogleBookDetailsService.getBookDetailsFromIsbn(isbn);
    }

    public static List<Book> getBooksFromTitle(String title){
        return GoogleBookDetailsService.getBooksBasedOnTitle(title);
    }
}
