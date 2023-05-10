package integrationTest;

import bookclub.annotations.Purgeable;
import bookclub.enums.StatusEnum;
import bookclub.models.Book;
import bookclub.services.DataPurgingService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class DataPurgingServiceTest {
    @Autowired
    private DataPurgingService dataPurgingService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testPurgeData() {
        // given
        Book book1 = new Book("Title1", "Author1", "1234567890", "Description1", "http://example.com/book1.jpg");
        book1.setStatus(StatusEnum.DELETED); // mark as deleted
        entityManager.persist(book1);

        Book book2 = new Book("Title2", "Author2", "0987654321", "Description2", "http://example.com/book2.jpg");
        book2.setStatus(StatusEnum.COMPLETED); // not deleted
        entityManager.persist(book2);

        // when
        dataPurgingService.purgeData();

        // then
        List<Book> books = entityManager.createQuery("SELECT b FROM Book b", Book.class).getResultList();
        assertEquals(1, books.size());
        assertTrue(books.contains(book2));
    }

    @Test
    public void testPurgeTablesAnnotation() {
        // given
        Purgeable purgeable = Book.class.getAnnotation(Purgeable.class);
        int expectedStatusThreshold = 99;

        // when

        // then
        assertEquals(expectedStatusThreshold, purgeable.statusThreshold());
    }
}
