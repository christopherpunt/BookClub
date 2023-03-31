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
    UserRepository userDao;

    public List<Book> getAllBooksForUser(String username){
        Optional<User> user = userDao.findByEmail(username);
        List<Book> books = new ArrayList<>();

        user.ifPresent(u -> books.addAll(bookDao.findAllBooksForUser(u.getId())));

        return books;
    }

    public void deleteBook(int id){
        bookDao.deleteById(id);
    }

    public boolean updateAllDetails(Book book) {
        Optional<Book> foundBook = bookDao.findById(book.getId());
        Book bookToSave;

        if (foundBook.isPresent()){
            bookToSave = foundBook.get();
            bookToSave.setTitle(book.getTitle());
            bookToSave.setAuthor(book.getAuthor());
            bookToSave.setIsbn(book.getIsbn());
            bookToSave.setDescription(book.getDescription());
            bookToSave.setBorrowedFromUser(book.getBorrowedFromUser());
            bookToSave.setLentToUser(book.getLentToUser());
            bookDao.save(bookToSave);
            return true;
        }
        return false;
    }

    public boolean addBookForUser(String username, Book book) {
        Optional<User> user = userDao.findByEmail(username);

        if (user.isPresent()) {
            book.setUser(user.get());
            //TODO: fix description when its too long
            book.setDescription("");
            bookDao.save(book);
            return true;
        }
        return false;
    }
}
