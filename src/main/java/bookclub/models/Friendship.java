package bookclub.models;

import bookclub.annotations.Purgeable;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Purgeable
public class Friendship extends BaseEntity{

    public Friendship(User user, User friend){
        this.user = user;
        this.friend = friend;
    }

    public Friendship() {}

    @ManyToOne
    private User user;

    @ManyToOne
    private User friend;

}
