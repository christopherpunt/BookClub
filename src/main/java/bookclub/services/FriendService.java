package bookclub.services;

import bookclub.models.Book;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendService {
    @Autowired
    private UserRepository userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private BookRepository bookDao;

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

    public List<Book> findAllFriendsBooks(String userEmail){
        Optional<User> userOptional = userDao.findByEmail(userEmail);

        if (userOptional.isPresent() && userOptional.get().getFriends() != null) {
            User user = userOptional.get();
            List<Integer> friendIds = user.getFriends().stream().map(User::getId).collect(Collectors.toList());
            return bookDao.findByUserIdIn(friendIds);
        }

        return Collections.emptyList();
    }
}
