package bookclub;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BookClubServer {
    public static void main(String[] args){ SpringApplication.run(BookClubServer.class, args); }

    public BookClubServer(String[] ignoredArgs) {
    }
}
