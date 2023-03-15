package bookclub.repositories;

import bookclub.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM User u WHERE u.email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);
}