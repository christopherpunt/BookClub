package bookclub.config;

import bookclub.enums.UserRoleEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests()
                .requestMatchers("/login", "/register").permitAll() // Allow access to login and register pages for everyone
                .requestMatchers("/admin/**").hasAuthority(UserRoleEnum.ADMIN.name()) // Restrict access to /users page to only ADMIN users
                .anyRequest().authenticated() // Require authentication for all other pages
            .and()
            .formLogin() // Configure login form
                .loginPage("/login") // Specify the login page URL
                .defaultSuccessUrl("/", true) // Redirect to "/" (root) page after successful login
            .and()
            .logout() // Configure logout
                .logoutUrl("/logout") // Specify the logout URL
                .logoutSuccessUrl("/login?logout") // Redirect to login page with logout parameter
            .and()
            .csrf().disable(); // Disable CSRF protection for simplicity (you may want to enable it in production)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
