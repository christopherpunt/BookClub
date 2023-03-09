import bookclub.book.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookTest {
    private final Book book = new Book();

    @Test
    void addition(){
        assertEquals(2, 1+1);
    }
}
