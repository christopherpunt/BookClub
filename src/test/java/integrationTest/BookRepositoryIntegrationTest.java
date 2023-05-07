package integrationTest;

import bookclub.models.Book;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.UserTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookRepositoryIntegrationTest extends BaseIntegrationTest {

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
}
