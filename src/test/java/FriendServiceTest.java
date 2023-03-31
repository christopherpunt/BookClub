import bookclub.models.User;
import bookclub.repositories.UserRepository;
import bookclub.services.FriendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FriendServiceTest {

    @Mock
    private UserRepository userDao;

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

        User returnedUser = friendService.addNewFriend(user, friend);

        assertNotNull(returnedUser);
        verify(userDao).save(returnedUser);

        assertEquals(1, returnedUser.getFriends().size());
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
