package bookclub.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Friendship {

    public Friendship(){}

    public Friendship(User user, User friend){
        this.user = user;
        this.friend = friend;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private User friend;

}
