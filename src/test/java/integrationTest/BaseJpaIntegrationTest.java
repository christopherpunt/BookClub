package integrationTest;

import bookclub.BookClubServer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BookClubServer.class)
@TestPropertySource(locations="classpath:test.properties")
@DataJpaTest
public abstract class BaseJpaIntegrationTest {
}
