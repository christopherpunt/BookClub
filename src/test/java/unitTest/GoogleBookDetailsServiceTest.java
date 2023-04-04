package unitTest;

import bookclub.models.Book;
import bookclub.services.GoogleBookDetailsService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GoogleBookDetailsServiceTest {

    @Test
    public void getBookDetailsTest(){
        Optional<Book> bookOptional = GoogleBookDetailsService.getBookDetailsFromIsbn("0545162076");
        Book book = null;
        if (bookOptional.isPresent()){
            book = bookOptional.get();
        }

        assertNotNull(book);
        assertEquals("Harry Potter the Complete Series", book.getTitle());
        assertEquals("J. K. Rowling", book.getAuthor());
        assertEquals("9780545162074", book.getIsbn());
        assertEquals("Collects the complete series that relates the adventures of young Harry Potter, who attends Hogwarts School of Witchcraft and Wizardry, where he and others of his kind learn their craft.", book.getDescription());
    }

    @Test
    public void getBooksFromTitle(){
        List<Book> books = GoogleBookDetailsService.getBooksBasedOnTitle("A time to kill");
        assertEquals(40, books.size());
    }

}
