package integrationTest;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@TestPropertySource(locations="classpath:test.properties")
@ActiveProfiles("test")
@ComponentScan("bookclub.repositories")
public abstract class BaseIntegrationTest {

}
