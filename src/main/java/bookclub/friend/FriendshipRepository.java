package bookclub.friend;

import bookclub.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query("SELECT f FROM Friendship f WHERE (f.user = :user AND f.friend = :friend) OR (f.user = :friend AND f.friend = :user)")
    Optional<Friendship> findFriendship(User user, User friend);

    List<Friendship> findAllFriendshipsByUser(User user);

    List<Friendship> findAllFriendshipsByFriend(User friend);
}
