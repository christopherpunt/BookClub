package unitTest;

import bookclub.book.Book;
import bookclub.book.GoogleBookDetailsService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GoogleBookDetailsServiceTest {

    @Test
    @Disabled
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
    }

    @Test
    @Disabled
    public void getBooksFromTitle(){
        List<Book> books = GoogleBookDetailsService.getBooksBasedOnTitle("A time to kill");
        assertEquals(40, books.size());
    }

}

