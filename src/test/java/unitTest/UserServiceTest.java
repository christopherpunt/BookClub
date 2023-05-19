package unitTest;

import bookclub.enums.UserRoleEnum;
import bookclub.models.User;
import bookclub.models.UserRole;
import bookclub.repositories.UserRepository;
import bookclub.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import utils.UserTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest extends BaseUnitTest{
    @Mock
    private UserRepository userRepo;

    @InjectMocks
    @Spy
    private UserService userService;

    @Test
    public void createUserGoldenPath(){
        //arrange
        String password = "solofest";
        User user = UserTestUtils.createUser("Sydney", "Punt", "sydney@email.com", password);
        when(userRepo.save(user)).thenReturn(user);

        //act
        userService.createUser(user);

        //assert
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(captor.capture());

        assertEquals(user.getFirstName(), captor.getValue().getFirstName());
        assertEquals(user.getLastName(), captor.getValue().getLastName());
        assertEquals(user.getEmail(), captor.getValue().getEmail());
        assertTrue(captor.getValue().isRegistered());

        assertNotEquals(password, user.getPassword());
        String encodedPassword = captor.getValue().getPassword();
        assertTrue(new BCryptPasswordEncoder().matches(password, encodedPassword));
    }

    @Test
    public void createUserAlreadyRegisteredExists(){
        //arrange
        User user = UserTestUtils.createUser("Sydney", "Punt", "smfrelier@gmail.com", "password");
        user.setRegistered(true);

        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        //act
        AuthenticationServiceException exception = assertThrows(AuthenticationServiceException.class, () -> userService.createUser(user));

        //assert
        assertEquals("A registered user with that email already exists", exception.getMessage());

        verify(userRepo).findByEmail(user.getEmail());
        verifyNoMoreInteractions(userRepo);
    }

    @Test
    public void createUserUnregisteredExists(){
        //arrange
        String password = "password";
        User user = UserTestUtils.createUser("Chris", "Punt", "chrispunt@email.com", null);
        User friend = UserTestUtils.createUser("Sydney", "Punt", "sydneypunt@email.com", null);
        user.setRegistered(false);
        user.addNewFriend(friend);

        User newUser = UserTestUtils.createUser("Chris2", "Punt2", "chrispunt@email.com", password);
        when(userRepo.findByEmail(newUser.getEmail())).thenReturn(Optional.of(user));

        //act
        userService.createUser(newUser);

        //assert
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).findByEmail(newUser.getEmail());
        verify(userRepo).save(captor.capture()); //save the original user but with new values from newUser

        assertEquals(newUser.getFirstName(), captor.getValue().getFirstName());
        assertEquals(newUser.getLastName(), captor.getValue().getLastName());
        assertEquals(newUser.getEmail(), captor.getValue().getEmail());
        assertEquals(friend, captor.getValue().getFriends().stream().findFirst().get());
        assertTrue(captor.getValue().isRegistered());

        assertNotEquals(password, captor.getValue().getPassword());
        assertNotEquals(password, user.getPassword());
        String encodedPassword = captor.getValue().getPassword();
        assertTrue(new BCryptPasswordEncoder().matches(password, encodedPassword));
    }

    @Test
    public void createUserUnRegisteredUser(){
        //arrange
        User user = UserTestUtils.createUser("Chris", "Punt", "chrisPunt@email.com", null);

        //act
        userService.createUnregisteredUser(user);

        //assert
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(captor.capture());

        assertFalse(captor.getValue().isRegistered());
    }

    @Test
    public void createUserUnRegisteredUserAlreadyExists(){
        //arrange
        User user = UserTestUtils.createUser("Chris", "Punt", "chrisPunt@email.com", null);

        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        //act
        AuthenticationServiceException exception = assertThrows(AuthenticationServiceException.class, () -> userService.createUnregisteredUser(user));

        //assert
        assertEquals("A user with that email already exists", exception.getMessage());

        verify(userRepo).findByEmail(user.getEmail());
        verifyNoMoreInteractions(userRepo);
    }

    @Test
    public void loadByUsernameTest(){
        //arrange
        User user = UserTestUtils.createUser("Sydney", "Punt", "smfrelier@gmail.com", "solofest");
        when(userRepo.findByEmail(user.email)).thenReturn(Optional.of(user));
        UserRole userRole = new UserRole(UserRoleEnum.USER);
        user.setUserRoles(List.of(userRole));

        //act
        UserDetails returned = userService.loadUserByUsername(user.getEmail());

        //assert
        assertEquals(user.getEmail(), returned.getUsername());
        assertEquals(user.getPassword(), returned.getPassword());
        assertEquals(1, returned.getAuthorities().size());
        assertEquals(UserRoleEnum.USER.name(), returned.getAuthorities().stream().findFirst().get().getAuthority());
    }

    @Test
    public void loadByUsernameDoesntExistTest(){
        //arrange
        User user = UserTestUtils.createUser("Sydney", "Punt", "smfrelier@gmail.com", "solofest");
        when(userRepo.findByEmail(user.email)).thenReturn(Optional.empty());

        //act
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(user.getEmail()));

        //assert
        assertEquals("Could not find username with email: " + user.getEmail(), exception.getMessage());
    }
}