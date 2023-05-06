package integrationTest;

import bookclub.models.Book;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import utils.UserTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class BookRepositoryIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = UserTestUtils.createUser("Test User");
        testUser = userRepository.save(testUser);
    }

    @AfterEach
    public void tearDown() {
        userRepository.delete(testUser);
    }

    @Test
    public void testSaveBook() {
        // Given
        Book book = new Book("Test Book", "Test Author", "1234567890", "Test Description", "https://example.com/book-cover.jpg");
        book.setUser(testUser);

        // When
        Book savedBook = bookRepository.save(book);

        // Then
        assertNotNull(savedBook.getId());
        assertEquals(book.getTitle(), savedBook.getTitle());
        assertEquals(book.getAuthor(), savedBook.getAuthor());
        assertEquals(book.getIsbn(), savedBook.getIsbn());
        assertEquals(book.getDescription(), savedBook.getDescription());
        assertEquals(book.getBookCoverUrl(), savedBook.getBookCoverUrl());
    }

    @Test
    public void testFindBookByUserAndIsbn() {
        // Given
        Book book = new Book("Test Book", "Test Author", "1234567890", "Test Description", "https://example.com/book-cover.jpg");
        book.setUser(testUser);
        bookRepository.save(book);

        // When
        Book foundBook = bookRepository.findByUserAndIsbn(testUser, "1234567890");

        // Then
        assertNotNull(foundBook);
        assertEquals(book.getTitle(), foundBook.getTitle());
        assertEquals(book.getAuthor(), foundBook.getAuthor());
        assertEquals(book.getIsbn(), foundBook.getIsbn());
        assertEquals(book.getDescription(), foundBook.getDescription());
        assertEquals(book.getBookCoverUrl(), foundBook.getBookCoverUrl());
    }

    @Test
    public void testFindBooksByUserIdIn() {
        // Given
        User anotherUser = UserTestUtils.createUser("another user");
        anotherUser = userRepository.save(anotherUser);

        Book book1 = new Book("Test Book 1", "Test Author", "1234567890", "Test Description", "https://example.com/book-cover.jpg");
        book1.setUser(testUser);
        bookRepository.save(book1);

        Book book2 = new Book("Test Book 2", "Test Author", "0987654321", "Test Description", "https://example.com/book-cover.jpg");
        book2.setUser(anotherUser);
        bookRepository.save(book2);

        // When
        List<Book> foundBooks = bookRepository.findByUserIdIn(Arrays.asList(testUser.getId(), anotherUser.getId()));

        // Then
        assertEquals(2, foundBooks.size());
        assertTrue(foundBooks.stream().anyMatch(book -> book.getTitle().equals(book1.getTitle())));
        assertTrue(foundBooks.stream().anyMatch(book -> book.getTitle().equals(book2.getTitle())));

        // Clean up
        userRepository.delete(anotherUser);
    }

}
