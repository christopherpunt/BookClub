package bookclub.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
public class User  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int Id;
    @Column(nullable = false, unique = true)
    public String email;
    @Column(nullable = false, length = 30)
    public String firstName;
    @Column(nullable = false, length = 30)
    public String lastName;
    @Column(length = 128)
    public String password;

    @Column
    private boolean isRegistered;

    @OneToMany
    private List<Friendship> friendships;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("USER");
    }

    public List<User> getFriends() {
        List<User> friends = new ArrayList<>();
        for (Friendship friendship : friendships) {
            friends.add(friendship.getFriend());
        }
        return friends;
    }

    public boolean addNewFriend(User friend) {
        if (friendships == null) {
            friendships = new ArrayList<>();
        }

        for (Friendship friendship : friendships) {
            if (friendship.getFriend().equals(friend)) {
                return false;
            }
        }
        Friendship friendship = new Friendship(this, friend);
        friendships.add(friendship);
        return true;
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }
}
