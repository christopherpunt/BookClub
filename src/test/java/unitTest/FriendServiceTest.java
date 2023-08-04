package unitTest;

import bookclub.book.Book;
import bookclub.book.BookRepository;
import bookclub.friend.FriendService;
import bookclub.friend.Friendship;
import bookclub.friend.FriendshipRepository;
import bookclub.notification.NotificationService;
import bookclub.user.User;
import bookclub.user.UserRepository;
import bookclub.user.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import utils.FriendshipTestUtils;
import utils.UserTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FriendServiceTest extends BaseUnitTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private FriendshipRepository friendshipRepo;

    @Mock
    private UserService userService;

    @Mock
    private BookRepository bookRepo;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private FriendService friendService;

    @Test
    public void addFriendTest(){
        //arrange
        User user = UserTestUtils.createUser("Chris Punt");
        User friend = UserTestUtils.createUser("Sydney Punt");

        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepo.findByEmail(friend.getEmail())).thenReturn(Optional.of(friend));
        when(friendshipRepo.findFriendship(user, friend)).thenReturn(Optional.empty());

        //act
        boolean returnValue = friendService.addNewFriendship(user.getEmail(), friend);

        //assert
        assertTrue(returnValue);
        verify(notificationService).sendFriendRequest(user.getEmail(), friend.getId());
    }

    @Test
    public void doNotAddSelfAsFriendTest(){
        //arrange
        User user = UserTestUtils.createUser("Chris Punt");
        User friend = UserTestUtils.createUser("Chris Punt");

        //act
        boolean returnValue = friendService.addNewFriendship(user.getEmail(), friend);

        //assert
        assertFalse(returnValue);
        verifyNoInteractions(notificationService);
    }

    @Test
    public void addUnregisteredFriendTest(){
        //arrange
        User user = UserTestUtils.createUser("Chris Punt");
        User friend = UserTestUtils.createUser("Sydney Punt");

        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userService.createUnregisteredUser(friend)).thenReturn(friend);
        when(notificationService.sendFriendRequest(user.getEmail(), friend.getId())).thenReturn(true);

        //act
        boolean returnValue = friendService.addNewFriendship(user.getEmail(), friend);

        //assert
        assertTrue(returnValue);
        verify(userService).createUnregisteredUser(friend);
        verify(notificationService).sendFriendRequest(user.getEmail(), friend.getId());
    }

    @Test
    public void addFriendTestAlreadyFriends(){
        //arrange
        User user = UserTestUtils.createUser("Chris Punt");
        User friend = UserTestUtils.createUser("Sydney Punt");

        Friendship friendship = new Friendship(user, friend);

        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepo.findByEmail(friend.getEmail())).thenReturn(Optional.of(friend));
        when(friendshipRepo.findFriendship(user, friend)).thenReturn(Optional.of(friendship));

        //act
        friendService.addNewFriendship(user.getEmail(), friend);

        //assert
        verifyNoInteractions(notificationService);
        verifyNoMoreInteractions(friendshipRepo);
    }

    @Test
    public void findAllFriendsBooksTest() {
        //arrange
        User user = UserTestUtils.createUser("Chris Punt");
        User friend1 = UserTestUtils.createUser("Sydney Punt");
        User friend2 = UserTestUtils.createUser("Gail Punt");

        List<Friendship> friendships = FriendshipTestUtils.createFriendships(user, friend1, friend2);
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(friendshipRepo.findAllFriendshipsByUser(user)).thenReturn(friendships);

        //act
        List<Book> returnedBooks = friendService.findAllFriendsBooks(user.getEmail());

        //assert
        assertNotNull(returnedBooks);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Long>> userIdListCaptor = ArgumentCaptor.forClass(List.class);
        verify(bookRepo).findByUserIdIn(userIdListCaptor.capture());

        assertEquals(2, userIdListCaptor.getValue().size());
    }

    @Test
    public void findAllFriendsFromUser(){
        //arrange
        User user = UserTestUtils.createUser("Chris Punt");
        User friend1 = UserTestUtils.createUser("Sydney Punt");
        User friend2 = UserTestUtils.createUser("Sue Punt");
        User friend3 = UserTestUtils.createUser("Abby Punt");

        Friendship friendship1 = new Friendship(user, friend1);
        Friendship friendship2 = new Friendship(friend2, user);
        Friendship friendship3 = new Friendship(friend3, user);

        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(friendshipRepo.findAllFriendshipsByUser(user)).thenReturn(List.of(friendship1));
        when(friendshipRepo.findAllFriendshipsByFriend(user)).thenReturn(List.of(friendship2, friendship3));

        //act
        List<User> friends;
        friends = friendService.findAllFriendsFromUser(user.getEmail());

        //assert
        assertEquals(3, friends.size());
    }
}
