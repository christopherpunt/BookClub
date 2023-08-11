package utils;

import bookclub.book.Book;
import bookclub.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookTestUtils {

    public static Book createBook(String title, String author, String isbn) {
        Book book = new Book();
        book.setId(new Random().nextLong());
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        return book;
    }

    public static Book createBook(User user, String title, String author, String isbn) {
        Book book = new Book();
        book.setUser(user);
        book.setId(new Random().nextLong());
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        return book;
    }

    public static Book createOwnedBook(User user) {
        Book book = new Book();
        book.setId(new Random().nextLong());
        book.setUser(user);
        book.setOwner(true);
        book.setTitle(user.getFirstName() + "'s Book");
        return book;
    }

    public static Book createBorrowedBook(User owner, User borrower){
        Book book = new Book();
        book.setId(new Random().nextLong());
        book.setOwner(false);
        book.setUser(borrower);
        book.setBorrowedFromUser(owner);
        return book;

    }

    public static List<Book> createBooksForUser(int numBooks, User user){
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < numBooks; i++) {
            Book book = new Book();
            book.setId(new Random().nextLong());
            book.setTitle(user.getFirstName() + "'s book #" + i);
            book.setAuthor("Author #" + i);
            book.setOwner(true);
            book.setUser(user);
            book.setIsbn("12345" + i);

            books.add(book);
        }
        return books;
    }
}
