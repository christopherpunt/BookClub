package integrationTest;

import bookclub.book.Book;
import bookclub.book.BookRepository;
import bookclub.user.User;
import bookclub.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.BookTestUtils;
import utils.UserTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BookRepositoryTest extends BaseJpaIntegrationTest{

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    UserRepository userRepository;

    @Test
    public void testFindByUser() {
        User user1 = UserTestUtils.createUser("John Doe");
        User user2 = UserTestUtils.createUser("Jane Doe");

        userRepository.save(user1);
        userRepository.save(user2);

        Book book1 = BookTestUtils.createBook(user1, "Title1", "Author1", "1234567890", "Book 1");
        Book book2 = BookTestUtils.createBook(user1, "Title2", "Author2", "1234567890", "Book 2");
        Book book3 = BookTestUtils.createBook(user2, "Title3", "Author3", "1234567890", "Book 3");

        bookRepo.saveAll(Arrays.asList(book1, book2, book3));

        List<Book> booksForUser1 = bookRepo.findByUser(user1);
        assertEquals(2, booksForUser1.size());
        //TODO: add asserts for books

        List<Book> booksForUser2 = bookRepo.findByUser(user2);
        assertEquals(1, booksForUser2.size());
        //TODO: add asserts for books
    }
}
