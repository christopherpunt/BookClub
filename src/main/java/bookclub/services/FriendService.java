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

    public boolean addNewFriendship(User user, User friend){
        Optional<User> friendOptional = userDao.findByEmail(friend.getEmail());

        if(friendOptional.isPresent()){
            User foundFriend = friendOptional.get();
            return createMutualFriends(user, foundFriend);
        } else{
            User createdFriend = userService.createUnregisteredUser(friend);
            return createMutualFriends(user, createdFriend);
        }
    }

    private boolean createMutualFriends(User user, User friend){
        boolean userAddFriend;
        boolean friendAddFriend;

        userAddFriend = user.addNewFriend(friend);
        friendAddFriend = friend.addNewFriend(user);

        if (userAddFriend){
            userDao.save(user);
        }
        if (friendAddFriend){
            userDao.save(friend);
        }

        return userAddFriend || friendAddFriend;
    }
}
