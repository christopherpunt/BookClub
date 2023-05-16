package bookclub.models;

import bookclub.enums.UserRoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
    @OneToMany
    private List<UserRole> userRoles;

    public UserRole addUserRole(UserRoleEnum roleEnum){
        return new UserRole(this, roleEnum);
    }

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
