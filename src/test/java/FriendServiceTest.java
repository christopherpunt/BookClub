import bookclub.models.User;
import bookclub.repositories.UserRepository;
import bookclub.services.FriendService;
import bookclub.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FriendServiceTest {

    @Mock
    private UserRepository userDao;

    @Mock
    private UserService userService;

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


    private User createUser(String firstName, String lastName, String email, String password){
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
