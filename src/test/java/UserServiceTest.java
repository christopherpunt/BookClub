import bookclub.models.User;
import bookclub.repositories.UserRepository;
import bookclub.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
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

        User returnedUser = userService.createUser(user);

        verify(userDao).save(user);
        assertNotEquals(password, returnedUser.getPassword());
    }

    @Test
    public void createUserAlreadyExists(){
        String password = "solofest";
        User user1 = createUser("Sydney", "Punt", "smfrelier@gmail.com", password);
        User user2 = createUser("Chris", "Punt", "smfrelier@gmail.com", password);
        when(userDao.save(user1)).thenReturn(user1);
        userService.createUser(user1);
        when(userDao.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

        AuthenticationServiceException exception = assertThrows(AuthenticationServiceException.class, () -> userService.createUser(user2));
        assertEquals("A user with that email already exists", exception.getMessage());

        verify(userDao).save(any());
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