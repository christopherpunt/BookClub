package unitTest;

import bookclub.book.Book;
import bookclub.book.GoogleBookDetailsService;
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
        assertEquals("Harry Potter Paperback Boxed Set: Books 1-7", book.getTitle());
        assertEquals("J. K. Rowling", book.getAuthor());
        assertEquals("9780545162074", book.getIsbn());
        assertEquals("For the first time, Rowling's seven bestselling Harry Potter books are available in a paperback boxed set. This is the ultimate Harry Potter collection for Potter fans.", book.getDescription());
    }

    @Test
    public void getBooksFromTitle(){
        List<Book> books = GoogleBookDetailsService.getBooksBasedOnTitle("A time to kill");
        assertEquals(40, books.size());
    }

}

