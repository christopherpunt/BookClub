package bookclub.user;

import bookclub.friend.Friendship;
import bookclub.notification.Notification;
import bookclub.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class User extends BaseEntity {
    @Column(nullable = false, unique = true)
    public String email;
    @Column(length = 30)
    public String firstName;
    @Column(length = 30)
    public String lastName;
    @Column(length = 128)
    public String password;
    @Column
    private boolean isRegistered;
    @OneToMany
    private List<Friendship> friendships;
    @OneToMany
    private List<Notification> notifications;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<UserRole> userRoles = new ArrayList<>();

    public List<User> getFriends() {
        List<User> friends = new ArrayList<>();
        for (Friendship friendship : friendships) {
            friends.add(friendship.getFriend());
        }
        return friends;
    }

    public Friendship addNewFriend(User friend) {
        if (friendships == null) {
            friendships = new ArrayList<>();
        }

        for (Friendship friendship : friendships) {
            if (friendship.getFriend().equals(friend)) {
                return null;
            }
        }

        Friendship friendship = new Friendship(this, friend);
        friendships.add(friendship);
        return friendship;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }
}
