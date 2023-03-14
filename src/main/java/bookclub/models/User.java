package bookclub.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int Id;
    @Column(nullable = false, unique = true)
    public String email;
    @Column(nullable = false, length = 30)
    public String firstName;
    @Column(nullable = false, length = 30)
    public String lastName;
    @Column(nullable = false, length = 64)
    public String password;
}
