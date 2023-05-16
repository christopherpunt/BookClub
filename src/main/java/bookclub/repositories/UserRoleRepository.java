package bookclub.repositories;

import bookclub.enums.UserRoleEnum;
import bookclub.models.User;
import bookclub.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findAllUserRolesByUser(User user);
    boolean existsByUserAndUserRole(User user, UserRoleEnum userRole);
}
