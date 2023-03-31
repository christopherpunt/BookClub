package bookclub.services;

import bookclub.models.User;
import bookclub.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendService {
    @Autowired
    private UserRepository userDao;

    public User addNewFriend(User user, User friend){
        Optional<User> friendOptional = userDao.findByEmail(friend.getEmail());

        if(friendOptional.isPresent()){
            User foundFriend = friendOptional.get();
            List<User> friends = user.getFriends();
            if (friends == null){
                friends = new ArrayList<User>();
                friends.add(foundFriend);
                user.setFriends(friends);
            } else{
                friends.add(foundFriend);
            }

            userDao.save(user);
        }

        return user;
    }
}
