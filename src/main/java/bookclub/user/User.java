package bookclub.user;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int Id;
    @Column
    public String firstName;
    @Column
    public String lastName;
    @Column
    public String username;
    @Column
    public String password;
}
