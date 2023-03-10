package bookclub.library;

import bookclub.book.Book;
import bookclub.user.User;
import jakarta.persistence.*;

import java.util.List;

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

    @OneToMany(mappedBy = "library")
    public List<Book> Books;
}
