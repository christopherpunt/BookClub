package integrationTest;

import bookclub.user.User;
import bookclub.user.UserRepository;
import bookclub.user.UserRole;
import bookclub.user.UserRoleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utils.UserTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserRepositoryTest extends BaseJpaIntegrationTest{
    @Autowired
    UserRepository userRepo;

    @Test
    public void getUserWithRolesTest(){
        User user = UserTestUtils.createUser("Chris Punt");
        UserRole userRole = new UserRole(UserRoleEnum.ADMIN);
        user.setUserRoles(List.of(userRole));
        userRepo.save(user);

        List<User> returnedUsers = userRepo.findAll();
        assertEquals(1, returnedUsers.size());
        assertEquals(1, returnedUsers.stream().findFirst().get().getUserRoles().size());
        assertEquals(UserRoleEnum.ADMIN, returnedUsers.stream().findFirst().get().getUserRoles().stream().findFirst().get().getUserRole());
    }
}
