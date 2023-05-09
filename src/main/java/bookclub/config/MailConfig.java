package bookclub.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class MailConfig {

    @Value("smtp.gmail.com")
    private String host;

    @Value("587")
    private int port;

    @Value("${senderEmail}")
    private String username;

    @Value("${senderEmailPassword}")
    private String password;

    @Value("true")
    private boolean smtpAuth;

    @Value("true")
    private boolean startTls;

}
