package bookclub.services;

import bookclub.models.Book;
import bookclub.models.Friendship;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.FriendshipRepository;
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

    @Autowired
    private FriendshipRepository friendshipDao;

    @Autowired
    private UserService userService;

    @Autowired
    private BookRepository bookDao;

    public Friendship addNewFriendship(User user, User friend){
        //if friend already exists
        //TODO: maybe should do a findByUser method
        Optional<User> friendOptional = userDao.findByEmail(friend.getEmail());

        Friendship friendship;

        if(friendOptional.isPresent()){
            User foundFriend = friendOptional.get();
            Optional<Friendship> friendshipOptional = friendshipDao.findFriendship(user, foundFriend);
            //if friendship already exists
            if (friendshipOptional.isPresent()){
                return friendshipOptional.get();
            }

            friendship = new Friendship(user, foundFriend);
        } else{
            User createdFriend = userService.createUnregisteredUser(friend);
            friendship = new Friendship(user, createdFriend);
        }

        friendshipDao.save(friendship);

        return friendship;
    }

    public List<User> findAllFriendsFromUser(User user){
        List<Friendship> friendships = friendshipDao.findAllFriendshipsByUser(user);

        List<User> friends = new ArrayList<>();

        for (Friendship friendship : friendships) {
            friends.add(friendship.getFriend());
        }

        return friends;
    }

    public List<Book> findAllFriendsBooks(String userEmail){
        Optional<User> userOptional = userDao.findByEmail(userEmail);

        List<Book> books = new ArrayList<>();

        if (userOptional.isPresent()){
            List<User> friends = findAllFriendsFromUser(userOptional.get());
            for (User user : friends) {
                books.addAll(bookDao.findByUser(user));
            }
        }

        return books;
    }
}
