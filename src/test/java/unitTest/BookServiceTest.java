package unitTest;

import bookclub.models.Book;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.UserRepository;
import bookclub.services.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import utils.BookTestUtils;
import utils.UserTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest extends BaseUnitTest {

    @Mock
    BookRepository bookDao;

    @Mock
    UserRepository userDao;

    @InjectMocks
    BookService bookService;

    @Test
    public void getAllUserBooksTest(){
        //arrange
        User user = UserTestUtils.createUser("Chris Punt", "chris@email.com");
        List<Book> books = BookTestUtils.createBooksForUser(10, user);

        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(bookDao.findByUser(user)).thenReturn(books);

        //act
        List<Book> returnedBooks = bookService.getAllBooksForUser(user.getEmail());

        //assert
        assertEquals(books.size(), returnedBooks.size());
    }
    @Test
    public void getAllUserBooksNoUserTest(){
        //arrange
        User user = UserTestUtils.createUser("Chris Punt", "non-user@email.com");

        //act
        List<Book> returnedBooks = bookService.getAllBooksForUser(user.getEmail());

        //assert
        verify(userDao).findByEmail(user.getEmail());
        verifyNoInteractions(bookDao);

        assertNotNull(returnedBooks);
        assertEquals(0, returnedBooks.size());
    }

    @Test
    public void addBookAsOwnedByUserTest(){
        //arrange
        User user = UserTestUtils.createUser("Chris Punt", "chris@email.com");
        Book book = BookTestUtils.createBook("Title", "Author", "Description", "ISBN");

        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        //act
        boolean returnValue = bookService.newBookForOwner(user.getEmail(), book);

        //assert
        assertTrue(returnValue);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookDao).save(bookCaptor.capture());
        assertTrue(bookCaptor.getValue().isOwner());
        assertEquals(user, bookCaptor.getValue().getUser());
    }

    @Test
    public void lendBookTest(){
        //arrange
        User giver = UserTestUtils.createUser("Chris Punt", "chris@email.com");
        User receiver = UserTestUtils.createUser("Sydney Punt", "sydney@email.com");
        Book book = BookTestUtils.createOwnedBook(giver);

        when(bookDao.findById(book.getId())).thenReturn(Optional.of(book));

        //act
        boolean returnValue = bookService.lendBook(book.getId(),giver, receiver);

        //assert
        assertTrue(returnValue);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookDao, times(2)).save(bookCaptor.capture());

        //lenders book
        assertEquals(giver, bookCaptor.getAllValues().get(0).getUser());
        assertTrue(bookCaptor.getAllValues().get(0).isOwner());
        assertEquals(receiver, bookCaptor.getAllValues().get(0).getLentToUser());
        assertNull(bookCaptor.getAllValues().get(0).getBorrowedFromUser());

        //Borrowers book
        assertEquals(receiver, bookCaptor.getAllValues().get(1).getUser());
        assertFalse(bookCaptor.getAllValues().get(1).isOwner());
        assertNull(bookCaptor.getAllValues().get(1).getLentToUser());
        assertEquals(giver, bookCaptor.getAllValues().get(1).getBorrowedFromUser());
    }

    @Test
    public void returnBookTest(){
        //arrange
        User owner = UserTestUtils.createUser("Chris Punt", "chris@email.com");
        User borrower = UserTestUtils.createUser("Sydney Punt", "sydney@email.com");
        Book ownersBook = BookTestUtils.createOwnedBook(owner);
        Book borrowersBook = BookTestUtils.createBorrowedBook(owner, borrower);

        when(bookDao.findById(borrowersBook.getId())).thenReturn(Optional.of(borrowersBook));
        when(userDao.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookDao.findByUserAndIsbn(owner, borrowersBook.getIsbn())).thenReturn(ownersBook);

        //act
        boolean returnValue = bookService.returnBook(borrowersBook.getId(), owner.getId());

        //assert
        assertTrue(returnValue);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookDao, times(2)).save(bookCaptor.capture());

        List<Book> savedBooks = bookCaptor.getAllValues();

        //borrowers book
        assertNull(savedBooks.get(0).getBorrowedFromUser());
        assertFalse(savedBooks.get(0).isOwner());
        assertEquals(borrower, savedBooks.get(0).getUser());

        //owners book
        assertNull(savedBooks.get(1).getLentToUser());
        assertEquals(owner, savedBooks.get(1).getUser());
        assertTrue(savedBooks.get(1).isOwner());


    }
}
