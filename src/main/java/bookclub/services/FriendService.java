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
import java.util.stream.Collectors;

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

    @Autowired
    private NotificationService notificationService;

    public boolean addNewFriendship(String userEmail, User createdFriend){
        Optional<User> userOptional = userDao.findByEmail(userEmail);
        Optional<User> friendOptional = userDao.findByEmail(createdFriend.getEmail());

        if (userOptional.isEmpty()){
            return false;
        }
        User user = userOptional.get();

        if (friendOptional.isEmpty()){
            User newFriend = userService.createUnregisteredUser(createdFriend);
            return notificationService.sendFriendRequest(user.getEmail(), newFriend.getId());
        }
        User foundFriend = friendOptional.get();

        Optional<Friendship> friendshipOptional = friendshipDao.findFriendship(user, foundFriend);
        //if friendship already exists can't create new friendship
        if (friendshipOptional.isPresent()){
            return false;
        }

        notificationService.sendFriendRequest(user.getEmail(), foundFriend.getId());
        return true;
    }

    public List<User> findAllFriendsFromUser(String userEmail){
        Optional<User> userOptional = userDao.findByEmail(userEmail);

        if (userOptional.isEmpty()){
            return List.of();
        }

        User user = userOptional.get();

        List<Friendship> userFriendships = friendshipDao.findAllFriendshipsByUser(user);
        List<Friendship> friendFriendships = friendshipDao.findAllFriendshipsByFriend(user);

        List<User> friends = new ArrayList<>();

        friends.addAll(userFriendships.stream().map(Friendship::getFriend).toList());
        friends.addAll(friendFriendships.stream().map(Friendship::getUser).toList());

        return friends;
    }

    public List<Book> findAllFriendsBooks(String userEmail){
        List<User> friends = findAllFriendsFromUser(userEmail);
        return bookDao.findByUserIdIn(friends.stream()
                .map(User::getId).collect(Collectors.toList())).stream()
                .filter(Book::isOwner).toList();
    }

    public List<Book> findAllFriendsBooksMatchSearch(String userEmail, String searchTerm){
        List<Book> books = findAllFriendsBooks(userEmail);

        //filter books
        return books.stream().filter(b -> b.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) ||
                b.getAuthor().toLowerCase().contains(searchTerm.toLowerCase())).toList();
    }

    public void completeFriendship(String email, User friend) {
        Optional<User> userOptional = userDao.findByEmail(email);
        Optional<User> friendOptional = userDao.findByEmail(friend.getEmail());

        if (userOptional.isPresent() && friendOptional.isPresent()){
            friendshipDao.save(new Friendship(userOptional.get(), friendOptional.get()));
        }
    }
}
