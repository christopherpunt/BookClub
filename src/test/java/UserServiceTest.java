import bookclub.models.User;
import bookclub.repositories.UserRepository;
import bookclub.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userDao;

    @InjectMocks
    @Spy
    private UserService userService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createUserGoldenPath(){
        String password = "solofest";
        User user = createUser("Sydney", "Punt", "smfrelier@gmail.com", password);
        when(userDao.save(user)).thenReturn(user);

        userService.createUser(user);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userDao).save(captor.capture());

        assertEquals(user.getFirstName(), captor.getValue().getFirstName());
        assertEquals(user.getLastName(), captor.getValue().getLastName());
        assertEquals(user.getEmail(), captor.getValue().getEmail());
        assertEquals(user.getFriends(), captor.getValue().getFriends());
        assertTrue(captor.getValue().isRegistered());

        //TODO: assert correctly encrypted
        assertNotEquals(password, user.getPassword());
    }

    @Test
    public void createUserAlreadyRegisteredExists(){
        User user = createUser("Sydney", "Punt", "smfrelier@gmail.com", "password");
        user.setRegistered(true);

        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        AuthenticationServiceException exception = assertThrows(AuthenticationServiceException.class, () -> userService.createUser(user));
        assertEquals("A registered user with that email already exists", exception.getMessage());

        verify(userDao).findByEmail(user.getEmail());
        verifyNoMoreInteractions(userDao);
    }

    @Test
    public void createUserUnregisteredExists(){
        User user = createUser("Chris", "Punt", "chrispunt@email.com", null);
        User friend = createUser("Sydney", "Punt", "sydneypunt@email.com", "password");
        user.setRegistered(false);
        user.addNewFriend(friend);

        User newUser = createUser("Chris2", "Punt2", "chrispunt@email.com", "password");
        when(userDao.findByEmail(newUser.getEmail())).thenReturn(Optional.of(user));
        userService.createUser(newUser);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userDao).findByEmail(newUser.getEmail());
        verify(userDao).save(captor.capture()); //save the original user but with new values from newUser

        assertEquals(newUser.getFirstName(), captor.getValue().getFirstName());
        assertEquals(newUser.getLastName(), captor.getValue().getLastName());
        assertEquals(newUser.getEmail(), captor.getValue().getEmail());
        assertEquals(friend, captor.getValue().getFriends().stream().findFirst().get());
        assertTrue(captor.getValue().isRegistered());

        //TODO: test that this the new user password is encrypted to the captor getpassword
        assertNotEquals(newUser.getPassword(), captor.getValue().getPassword());
    }

    @Test
    public void createUserUnRegisteredUser(){
        User user = createUser("Chris", "Punt", "chrisPunt@email.com", null);

        userService.createUnregisteredUser(user);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userDao).save(captor.capture());

        assertFalse(captor.getValue().isRegistered());
    }

    @Test
    public void createUserUnRegisteredUserAlreadyExists(){
        User user = createUser("Chris", "Punt", "chrisPunt@email.com", null);

        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        AuthenticationServiceException exception = assertThrows(AuthenticationServiceException.class, () -> userService.createUnregisteredUser(user));
        assertEquals("A user with that email already exists", exception.getMessage());

        verify(userDao).findByEmail(user.getEmail());
        verifyNoMoreInteractions(userDao);
    }



    @Test
    public void loadByUsernameTest(){
        User user = createUser("Sydney", "Punt", "smfrelier@gmail.com", "solofest");
        when(userDao.findByEmail(user.email)).thenReturn(Optional.of(user));

        UserDetails returned = userService.loadUserByUsername(user.getEmail());

        assertEquals(user.getEmail(), returned.getUsername());
        assertEquals(user.getPassword(), returned.getPassword());
    }

    @Test
    public void loadByUsernameDoesntExistTest(){
        User user = createUser("Sydney", "Punt", "smfrelier@gmail.com", "solofest");
        when(userDao.findByEmail(user.email)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(user.getEmail()));
        assertEquals("Could not find username with email: " + user.getEmail(), exception.getMessage());
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