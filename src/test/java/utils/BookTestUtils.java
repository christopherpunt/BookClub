package utils;

import bookclub.models.Book;
import bookclub.models.User;

import java.util.ArrayList;
import java.util.List;

public class BookTestUtils {

    public static Book createBook(String title, String author, String isbn, String description) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setDescription(description);
        return book;
    }

    public static Book createOwnedBook(User user) {
        Book book = new Book();
        book.setUser(user);
        book.setOwner(true);
        return book;
    }

    public static Book createBorrowedBook(User owner, User borrower){
        Book book = new Book();
        book.setOwner(false);
        book.setUser(borrower);
        book.setBorrowedFromUser(owner);
        return book;

    }



    public static List<Book> createBooksForUser(int numBooks, User user){
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < numBooks; i++) {
            Book book = new Book();
            book.setTitle(user.getFirstName() + "'s book #" + i);
            book.setAuthor("Author #" + i);
            book.setOwner(true);
            book.setUser(user);
            book.setId((long) i);
            book.setIsbn("12345" + i);

            books.add(book);
        }
        return books;
    }
}
