package unitTest;

import bookclub.models.Friendship;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.FriendshipRepository;
import bookclub.repositories.UserRepository;
import bookclub.services.FriendService;
import bookclub.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import utils.FriendshipTestUtils;
import utils.UserTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static utils.UserTestUtils.createUser;

public class FriendServiceTest extends BaseUnitTest {

    @Mock
    private UserRepository userDao;

    @Mock
    private FriendshipRepository friendshipDao;

    @Mock
    private UserService userService;

    @Mock
    private BookRepository bookDao;

    @InjectMocks
    private FriendService friendService;

    @Test
    public void addFriendTest(){
        User user = createUser("Chris", "Punt", "chrispunt13@icloud.com", "password");
        User friend = createUser("Sydney", "Punt", "smfrelier@gmail.com", "password");

        when(friendshipDao.findFriendship(user, friend)).thenReturn(Optional.empty());
        when(userDao.findByEmail(friend.getEmail())).thenReturn(Optional.of(friend));

        Friendship friendship = friendService.addNewFriendship(user.getEmail(), friend);

        verify(friendshipDao).save(friendship);
    }

    @Test
    public void addMultipleFriendsTest(){
        User user = createUser("Chris", "Punt", "chrispunt13@icloud.com", "password");
        User friend = createUser("Sydney", "Punt", "smfrelier@gmail.com", "password");
        User friend2 = createUser("Abby", "Punt", "abbyPunt@gmail.com", "password");

        when(friendshipDao.findFriendship(user, friend)).thenReturn(Optional.empty());

        when(userDao.findByEmail(friend.getEmail())).thenReturn(Optional.of(friend));
        when(userDao.findByEmail(friend2.getEmail())).thenReturn(Optional.of(friend2));

        Friendship firstFriendShip = friendService.addNewFriendship(user.getEmail(), friend);
        Friendship secondFriendShip = friendService.addNewFriendship(user.getEmail(), friend2);

        verify(friendshipDao).save(firstFriendShip);
        verify(friendshipDao).save(secondFriendShip);
    }

    @Test
    public void addUnregisteredFriendTest(){
        User user = createUser("Chris", "Punt", "chrispunt13@icloud.com", "password");
        User friend = createUser("Sydney", "Punt", "smfrelier@gmail.com", null);

        when(userService.createUnregisteredUser(friend)).thenReturn(friend);

        Friendship friendship = friendService.addNewFriendship(user.getEmail(), friend);

        verify(friendshipDao).save(friendship);
    }

    @Test
    public void addFriendTestAlreadyFriends(){
        User user = createUser("Chris", "Punt", "chrispunt13@icloud.com", "password");
        User friend = createUser("Sydney", "Punt", "smfrelier@gmail.com", null);

        Friendship friendship = new Friendship(user, friend);

        when(userDao.findByEmail(friend.getEmail())).thenReturn(Optional.of(friend));
        when(friendshipDao.findFriendship(user, friend)).thenReturn(Optional.of(friendship));

        friendService.addNewFriendship(user.getEmail(), friend);

        verify(friendshipDao).findFriendship(user, friend);
        verifyNoMoreInteractions(friendshipDao);
    }

    @Test
    public void findAllFriendsBooksTest(){
        User user = UserTestUtils.createUser("Chris Punt", "chris@email.com");
        User friend1 = UserTestUtils.createUser("Sydney Punt", "sydney@email.com");

        Friendship friendship = FriendshipTestUtils.createFriendship(user, friend1);

        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        when(friendshipDao.findAllFriendshipsByUser(user)).

        BookTestUtils.createBooksForUser(10, user);



//        assertNotNull(f);

    }


    //TODO: re-enable test
//    @Test
//    public void findAllFriendsBooks(){
//
//        User user = createUser("Chris", "Punt", "chrispunt@email.com", "password");
//        User friend = createUser("Chris2", "Punt2", "chrispunt2@email", "password");
//        User friend2 = createUser("Chris3", "Punt3", "chrispunt3@email", "password");
//        User friend3 = createUser("Chris4", "Punt4", "chrispunt4@email", "password");
//
//        List<Book> friend1books = new ArrayList<>();
//        List<Book> friend2books = new ArrayList<>();
//        List<Book> friend3books = new ArrayList<>();
//
//        friend1books.add(createBook(friend));
//        friend1books.add(createBook(friend));
//        friend1books.add(createBook(friend));
//
//        friend2books.add(createBook(friend2));
//        friend2books.add(createBook(friend2));
//        friend2books.add(createBook(friend2));
//
//        friend3books.add(createBook(friend3));
//        friend3books.add(createBook(friend3));
//        friend3books.add(createBook(friend3));
//
//        List<Book> allBooks = new ArrayList<>();
//        allBooks.addAll(friend1books);
//        allBooks.addAll(friend2books);
//        allBooks.addAll(friend3books);
//
//
//        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//
//        when(bookDao.findByUserIdIn(List.of(friend.getId(), friend2.getId(), friend3.getId()))).thenReturn(allBooks);
//        List<Book> returnedBooks = friendService.findAllFriendsBooks(user.getEmail());
//
//        assertEquals(9, returnedBooks.size());
//    }

    @Test
    public void findAllFriendsFromUser(){
        User user = createUser("Chris", "Punt", "chrispunt@email.com", "password");
        User friend = createUser("Chris2", "Punt2", "chrispunt2@email.com", "password");
        User friend2 = createUser("Chris2", "Punt2", "chrispunt2@email.com", "password");

        Friendship friendship = new Friendship(user, friend);
        Friendship friendship2 = new Friendship(user, friend2);

        when(friendshipDao.findAllFriendshipsByUser(user)).thenReturn(List.of(friendship, friendship2));

        List<User> friends;
        friends = friendService.findAllFriendsFromUser(user);

        assertEquals(2, friends.size());
    }
}
