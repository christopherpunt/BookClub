package bookclub.services;

import bookclub.models.User;
import bookclub.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FriendService {
    @Autowired
    private UserRepository userDao;

    @Autowired
    private UserService userService;

    public User addNewFriend(User user, User friend){
        Optional<User> friendOptional = userDao.findByEmail(friend.getEmail());

        if(friendOptional.isPresent()){
            User foundFriend = friendOptional.get();
            user.addNewFriend(foundFriend);
        } else{
            User createdFriend = userService.createUnregisteredUser(friend);
            user.addNewFriend(createdFriend);
        }
        userDao.save(user);

        return user;
    }
}
