package bookclub.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userDao;

    public User createUser(User user) {
        return userDao.save(user);
    }

    public List<User> getUsers() {
        return userDao.findAll();
    }
}
