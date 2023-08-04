package bookclub.friend;

import bookclub.book.Book;
import bookclub.book.BookRepository;
import bookclub.notification.NotificationService;
import bookclub.user.User;
import bookclub.user.UserRepository;
import bookclub.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private FriendshipRepository friendshipRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private NotificationService notificationService;

    public boolean addNewFriendship(String userEmail, User createdFriend){

        if (Objects.equals(userEmail, createdFriend.getEmail())){
            return false;
        }

        Optional<User> userOptional = userRepo.findByEmail(userEmail);
        Optional<User> friendOptional = userRepo.findByEmail(createdFriend.getEmail());

        if (userOptional.isEmpty()){
            return false;
        }
        User user = userOptional.get();

        if (friendOptional.isEmpty()){
            User newFriend = userService.createUnregisteredUser(createdFriend);
            return notificationService.sendFriendRequest(user.getEmail(), newFriend.getId());
        }
        User foundFriend = friendOptional.get();

        Optional<Friendship> friendshipOptional = friendshipRepo.findFriendship(user, foundFriend);
        //if friendship already exists can't create new friendship
        if (friendshipOptional.isPresent()){
            return false;
        }

        notificationService.sendFriendRequest(user.getEmail(), foundFriend.getId());
        return true;
    }

    public List<User> findAllFriendsFromUser(String userEmail){
        Optional<User> userOptional = userRepo.findByEmail(userEmail);

        if (userOptional.isEmpty()){
            return List.of();
        }

        User user = userOptional.get();

        List<Friendship> userFriendships = friendshipRepo.findAllFriendshipsByUser(user);
        List<Friendship> friendFriendships = friendshipRepo.findAllFriendshipsByFriend(user);

        List<User> friends = new ArrayList<>();

        friends.addAll(userFriendships.stream().map(Friendship::getFriend).toList());
        friends.addAll(friendFriendships.stream().map(Friendship::getUser).toList());

        return friends;
    }

    public List<Book> findAllFriendsBooks(String userEmail){
        List<User> friends = findAllFriendsFromUser(userEmail);
        return bookRepo.findByUserIdIn(friends.stream()
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
        Optional<User> userOptional = userRepo.findByEmail(email);
        Optional<User> friendOptional = userRepo.findByEmail(friend.getEmail());

        if (userOptional.isPresent() && friendOptional.isPresent()){
            friendshipRepo.save(new Friendship(userOptional.get(), friendOptional.get()));
        }
    }
}
