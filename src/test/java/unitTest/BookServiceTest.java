package unitTest;

import bookclub.book.Book;
import bookclub.book.BookRepository;
import bookclub.book.BookService;
import bookclub.user.User;
import bookclub.user.UserRepository;
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
    BookRepository bookRepo;

    @Mock
    UserRepository userRepo;

    @InjectMocks
    BookService bookService;

    @Test
    public void getAllUserBooksTest(){
        //arrange
        User user = UserTestUtils.createUser("Chris Punt");
        List<Book> books = BookTestUtils.createBooksForUser(10, user);

        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(bookRepo.findByUser(user)).thenReturn(books);

        //act
        List<Book> returnedBooks = bookService.getAllBooksForUser(user.getEmail());

        //assert
        assertEquals(books.size(), returnedBooks.size());
    }
    @Test
    public void getAllUserBooksNoUserTest(){
        //arrange
        User user = UserTestUtils.createUser("Chris Punt");

        //act
        List<Book> returnedBooks = bookService.getAllBooksForUser(user.getEmail());

        //assert
        verify(userRepo).findByEmail(user.getEmail());
        verifyNoInteractions(bookRepo);

        assertNotNull(returnedBooks);
        assertEquals(0, returnedBooks.size());
    }

    @Test
    public void addBookAsOwnedByUserTest(){
        //arrange
        User user = UserTestUtils.createUser("Chris Punt");
        Book book = BookTestUtils.createBook("Title", "Author", "Description", "ISBN");

        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        //act
        boolean returnValue = bookService.newBookForOwner(user.getEmail(), book);

        //assert
        assertTrue(returnValue);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepo).save(bookCaptor.capture());
        assertTrue(bookCaptor.getValue().isOwner());
        assertEquals(user, bookCaptor.getValue().getUser());
    }

    @Test
    public void lendBookTest(){
        //arrange
        User giver = UserTestUtils.createUser("Chris Punt");
        User receiver = UserTestUtils.createUser("Sydney Punt");
        Book book = BookTestUtils.createOwnedBook(giver);

        when(bookRepo.findById(book.getId())).thenReturn(Optional.of(book));

        //act
        boolean returnValue = bookService.completeBorrowRequest(book.getId(),giver, receiver);

        //assert
        assertTrue(returnValue);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepo, times(2)).save(bookCaptor.capture());

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
        User owner = UserTestUtils.createUser("Chris Punt");
        User borrower = UserTestUtils.createUser("Sydney Punt");
        Book ownersBook = BookTestUtils.createOwnedBook(owner);
        Book borrowersBook = BookTestUtils.createBorrowedBook(owner, borrower);

        when(bookRepo.findById(borrowersBook.getId())).thenReturn(Optional.of(borrowersBook));
        when(userRepo.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(bookRepo.findByUserAndIsbn(owner, borrowersBook.getIsbn())).thenReturn(ownersBook);

        //act
        boolean returnValue = bookService.returnBook(borrowersBook.getId(), owner.getId());

        //assert
        assertTrue(returnValue);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepo, times(2)).save(bookCaptor.capture());

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
