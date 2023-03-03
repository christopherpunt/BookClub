package bookclub;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class BookClubServer {
    public static void main(String[] args){ SpringApplication.run(BookClubServer.class, args); }

    public BookClubServer(String[] ignoredArgs) {
    }
}
