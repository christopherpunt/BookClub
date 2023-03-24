import bookclub.models.Book;
import bookclub.services.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookServiceTest {
    @Autowired
    public BookService bookService;

    @Test
    public void getBookDetailsTest(){
        Book book = BookService.getBookDetails("0545162076");

        assertEquals("Harry Potter the Complete Series", book.getTitle());
        assertEquals("J. K. Rowling", book.getAuthor());
        assertEquals("9780545162074", book.getIsbn());
        assertEquals("Collects the complete series that relates the adventures of young Harry Potter, who attends Hogwarts School of Witchcraft and Wizardry, where he and others of his kind learn their craft.", book.getDescription());
    }

    @Test
    public void getBooksFromTitle(){
        List<Book> books = BookService.getBooksFromTitle("A time to kill");
        assertEquals(40, books.size());
    }

}

