package bookclub.repositories;

import bookclub.models.Friendship;
import bookclub.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
    @Query("SELECT f FROM Friendship f WHERE (f.user = :user AND f.friend = :friend) OR (f.user = :friend AND f.friend = :user)")
    Optional<Friendship> findFriendship(User user, User friend);

    @Query("SELECT f FROM Friendship f WHERE (f.user = :user OR f.friend = :user)")
    List<Friendship> findAllFriendshipsByUser(User user);
}
