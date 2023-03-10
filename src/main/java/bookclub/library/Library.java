package bookclub.library;

import bookclub.user.User;
import jakarta.persistence.*;

@Entity
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int Id;
    @Column
    public String Name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    public User User;
}
