package bookclub.friend;

import bookclub.shared.BaseEntity;
import bookclub.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Friendship extends BaseEntity {

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
