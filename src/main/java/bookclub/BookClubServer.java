package bookclub;


import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BookClubServer {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void setup(){
        Stripe.apiKey = stripeApiKey;
    }
    public static void main(String[] args){ SpringApplication.run(BookClubServer.class, args); }

    public BookClubServer(String[] ignoredArgs) {
    }
}
