package bookclub.book;

import bookclub.user.User;
import bookclub.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepo;

    @Autowired
    UserRepository userRepo;

    public List<Book> getAllBooksForUser(String username){
        Optional<User> userOptional = userRepo.findByEmail(username);
        if (userOptional.isPresent()){
            return bookRepo.findByUser(userOptional.get());
        }
        return Collections.emptyList();
    }

    public boolean updateAllDetails(Book book) {
        Optional<Book> foundBook = bookRepo.findById(book.getId());
        Book bookToSave;

        if (foundBook.isPresent()){
            bookToSave = foundBook.get();
            bookToSave.setTitle(book.getTitle());
            bookToSave.setAuthor(book.getAuthor());
            bookToSave.setIsbn(book.getIsbn());
            bookToSave.setBorrowedFromUser(book.getBorrowedFromUser());
            bookToSave.setLentToUser(book.getLentToUser());
            bookRepo.save(bookToSave);
            return true;
        }
        return false;
    }

    public boolean newBookForOwner(String username, Book book) {
        Optional<User> user = userRepo.findByEmail(username);

        if (user.isPresent()) {
            book.setUser(user.get());
            book.setOwner(true);
            bookRepo.save(book);
            return true;
        }
        return false;
    }

    public boolean completeBorrowRequest(Long bookId, User giver, User receiver){
        Optional<Book> bookOptional = bookRepo.findById(bookId);

        if (bookOptional.isEmpty()){
            return false;
        }
        Book book = bookOptional.get();
        book.setLentToUser(receiver);
        bookRepo.save(book);

        Book newBook = new Book();
        newBook.setUser(receiver);
        newBook.setBorrowedFromUser(giver);
        newBook.setBookCoverUrl(book.getBookCoverUrl());
        newBook.setTitle(book.getTitle());
        newBook.setAuthor(book.getAuthor());
        newBook.setIsbn(book.getIsbn());

        bookRepo.save(newBook);

        return true;
    }

    public boolean returnBook(Long bookId, Long ownerId) {
        Optional<Book> bookOptional = bookRepo.findById(bookId);
        Optional<User> ownerOptional = userRepo.findById(ownerId);

        if (bookOptional.isEmpty() || ownerOptional.isEmpty()){
            return false;
        }
        Book book = bookOptional.get();
        book.setBorrowedFromUser(null);
        bookRepo.save(book);

        User owner = ownerOptional.get();
        Book ownersBook = bookRepo.findByUserAndIsbn(owner, book.getIsbn());
        ownersBook.setLentToUser(null);
        bookRepo.save(ownersBook);

        return true;

    }
}
