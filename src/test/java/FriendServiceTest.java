import bookclub.models.Book;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.UserRepository;
import bookclub.services.FriendService;
import bookclub.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FriendServiceTest {

    @Mock
    private UserRepository userDao;

    @Mock
    private UserService userService;

    @Mock
    private BookRepository bookDao;

    @InjectMocks
    @Spy
    private FriendService friendService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addFriendTest(){
        User user = createUser("Chris", "Punt", "chrispunt13@icloud.com", "password");
        User friend = createUser("Sydney", "Punt", "smfrelier@gmail.com", "password");

        when(userDao.findByEmail(friend.getEmail())).thenReturn(Optional.of(friend));

        boolean friendAdded = friendService.addNewFriendship(user, friend);

        assertTrue(friendAdded);
        verify(userDao).save(user);
        verify(userDao).save(friend);

        assertEquals(1, user.getFriends().size());
        assertEquals(1, friend.getFriends().size());
    }

    @Test
    public void addMultipleFriendsTest(){
        User user = createUser("Chris", "Punt", "chrispunt13@icloud.com", "password");
        User friend = createUser("Sydney", "Punt", "smfrelier@gmail.com", "password");
        User friend2 = createUser("Abby", "Punt", "abbyPunt@gmail.com", "password");

        when(userDao.findByEmail(friend.getEmail())).thenReturn(Optional.of(friend));
        when(userDao.findByEmail(friend2.getEmail())).thenReturn(Optional.of(friend2));

        boolean friendAdded = friendService.addNewFriendship(user, friend);
        boolean friend2Added = friendService.addNewFriendship(user, friend2);

        assertTrue(friendAdded);
        assertTrue(friend2Added);
        verify(userDao, times(2)).save(user);
        verify(userDao).save(friend);
        verify(userDao).save(friend2);

        assertEquals(2, user.getFriends().size());
        assertEquals(1, friend.getFriends().size());
        assertEquals(1, friend2.getFriends().size());
    }

    @Test
    public void addUnregisteredFriendTest(){
        User user = createUser("Chris", "Punt", "chrispunt13@icloud.com", "password");
        User friend = createUser("Sydney", "Punt", "smfrelier@gmail.com", null);

        when(userService.createUnregisteredUser(friend)).thenReturn(friend);

        boolean friendAdded = friendService.addNewFriendship(user, friend);

        assertTrue(friendAdded);
        verify(userDao).save(user);
        verify(userDao).save(friend);

        assertEquals(1, user.getFriends().size());
    }

    @Test
    public void addFriendTestAlreadyFriends(){
        User user = createUser("Chris", "Punt", "chrispunt13@icloud.com", "password");
        User friend = createUser("Sydney", "Punt", "smfrelier@gmail.com", null);

        user.addNewFriend(friend);
        friend.addNewFriend(user);

        when(userDao.findByEmail(friend.getEmail())).thenReturn(Optional.of(friend));

        boolean friendAdded = friendService.addNewFriendship(user, friend);

        assertFalse(friendAdded);

        verify(userDao).findByEmail(friend.getEmail());
        verifyNoMoreInteractions(userDao);

        assertEquals(1, user.getFriends().size());
        assertEquals(1, friend.getFriends().size());
    }

    @Test
    public void addFriendTestOneAlreadyAFriend(){
        User user = createUser("Chris", "Punt", "chrispunt13@icloud.com", "password");
        User friend = createUser("Sydney", "Punt", "smfrelier@gmail.com", null);

        user.addNewFriend(friend);

        when(userDao.findByEmail(friend.getEmail())).thenReturn(Optional.of(friend));

        boolean friendAdded = friendService.addNewFriendship(user, friend);

        assertTrue(friendAdded);

        verify(userDao).findByEmail(friend.getEmail());
        verify(userDao).save(friend);
        verifyNoMoreInteractions(userDao);

        assertEquals(1, user.getFriends().size());
        assertEquals(1, friend.getFriends().size());
    }

    @Test
    public void findAllFriendsBooks(){

        User user = createUser("Chris", "Punt", "chrispunt@email.com", "password");
        User friend = createUser("Chris2", "Punt2", "chrispunt2@email", "password");
        User friend2 = createUser("Chris3", "Punt3", "chrispunt3@email", "password");
        User friend3 = createUser("Chris4", "Punt4", "chrispunt4@email", "password");

        user.addNewFriend(friend);
        user.addNewFriend(friend2);
        user.addNewFriend(friend3);

        List<Book> friend1books = new ArrayList<>();
        List<Book> friend2books = new ArrayList<>();
        List<Book> friend3books = new ArrayList<>();

        friend1books.add(createBook(friend));
        friend1books.add(createBook(friend));
        friend1books.add(createBook(friend));

        friend2books.add(createBook(friend2));
        friend2books.add(createBook(friend2));
        friend2books.add(createBook(friend2));

        friend3books.add(createBook(friend3));
        friend3books.add(createBook(friend3));
        friend3books.add(createBook(friend3));

        List<Book> allBooks = new ArrayList<>();
        allBooks.addAll(friend1books);
        allBooks.addAll(friend2books);
        allBooks.addAll(friend3books);


        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        when(bookDao.findByUserIdIn(List.of(friend.getId(), friend2.getId(), friend3.getId()))).thenReturn(allBooks);
        List<Book> returnedBooks = friendService.findAllFriendsBooks(user.getEmail());

        assertEquals(9, returnedBooks.size());
    }

    private Book createBook(User user) {
        Book book = new Book();
        book.setTitle(user.getFirstName() + "'s Book");
        book.setAuthor(user.getLastName());
        book.setIsbn(UUID.randomUUID().toString());
        book.setUser(user);
        return book;
    }


    private User createUser(String firstName, String lastName, String email, String password){
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
