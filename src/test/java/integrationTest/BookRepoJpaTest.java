package integrationTest;

import bookclub.BookClubServer;
import bookclub.models.Book;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import utils.BookTestUtils;
import utils.UserTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookClubServer.class)
@TestPropertySource(locations="classpath:test.properties")
@DataJpaTest
public class BookRepoJpaTest {

    @Autowired
    private BookRepository bookRepository;

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

        bookRepository.saveAll(Arrays.asList(book1, book2, book3));

        List<Book> booksForUser1 = bookRepository.findByUser(user1);
        assertEquals(2, booksForUser1.size());
//        assertThat(booksForUser1).containsExactlyInAnyOrder(book1, book2);

        List<Book> booksForUser2 = bookRepository.findByUser(user2);
        assertEquals(1, booksForUser2.size());
//        assertThat(booksForUser2).containsExactly(book3);
    }
}
