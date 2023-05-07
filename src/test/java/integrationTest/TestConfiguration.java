package integrationTest;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
//@EnableJpaRepositories(basePackages = "bookclub.repositories")
@Profile("test")
//@ComponentScan("bookclub.repositories")
public class TestConfiguration {

}
