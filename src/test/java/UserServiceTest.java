import bookclub.BookClubServer;
import bookclub.models.User;
import bookclub.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(classes = BookClubServer.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {
    @Autowired
    private UserRepository repo;

    @Test
    public void createUser(){
        User user = new User();
        user.setFirstName("Chris1");
        user.setLastName("Punt");
        user.setPassword("password");
        user.setEmail("chrispunt13@icloud.com");

        User savedUser = repo.save(user);

        User exitUser = repo.findById(savedUser.getId()).orElse(null);

        assertNotNull(exitUser);
        assertEquals(user.getFirstName(), exitUser.getFirstName());
        assertEquals(user.getLastName(), exitUser.getLastName());
        assertEquals(user.getPassword(), exitUser.getPassword());
        assertEquals(user.getEmail(), exitUser.getEmail());

    }
}