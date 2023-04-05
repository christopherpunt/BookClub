package bookclub.services;

import bookclub.models.Book;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    BookRepository bookDao;

    @Autowired
    UserRepository userDao;

    public List<Book> getAllBooksForUser(String username){
        Optional<User> userOptional = userDao.findByEmail(username);
        if (userOptional.isPresent()){
            return bookDao.findByUser(userOptional.get());
        }
        return Collections.emptyList();
    }

    public void deleteBook(Long id){
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

    public boolean lendBook(Long bookId, User giver, User receiver){
        Optional<Book> bookOptional = bookDao.findById(bookId);

        if (bookOptional.isEmpty()){
            return false;
        }
        Book book = bookOptional.get();
        book.setLentToUser(receiver);
        bookDao.save(book);

        Book newBook = new Book();
        newBook.setUser(receiver);
        newBook.setBorrowedFromUser(giver);
        newBook.setBookCoverUrl(book.getBookCoverUrl());
        newBook.setTitle(book.getTitle());
        newBook.setAuthor(book.getAuthor());
        newBook.setDescription(book.getDescription());
        newBook.setIsbn(book.getIsbn());

        bookDao.save(newBook);

        return true;
    }
}
