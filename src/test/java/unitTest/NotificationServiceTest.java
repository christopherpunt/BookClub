package unitTest;

import bookclub.models.Book;
import bookclub.models.Notification;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.NotificationRepository;
import bookclub.repositories.UserRepository;
import bookclub.services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utils.UserUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationDao;

    @Mock
    private UserRepository userDao;

    @Mock
    private BookRepository bookDao;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void sendBorrowRequestNotificationTest(){
        User user = UserUtils.createUser("Chris Punt", "chrispunt@email.com");
        User friend = UserUtils.createUser("Chris2 Punt2", "chrispunt2@email.com");
        Book book = new Book();

        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userDao.findById(friend.getId())).thenReturn(Optional.of(friend));
        when(bookDao.findById(book.getId())).thenReturn(Optional.of(book));

        boolean returnedStatus = notificationService.sendBorrowRequest(user.getEmail(), friend.getId(), book.getId());

        //TODO: Argument captor?

        verify(notificationDao).save(any());
    }

    @Test
    public void sendFriendRequestNotificationTest(){
        User user = UserUtils.createUser("Chris Punt", "chrispunt@email.com");
        User sender = UserUtils.createUser("Chris2 Punt2", "chrispunt2@email.com");

        Notification notification = notificationService.sendFriendRequest(user.getEmail(), sender.getId());

        //TODO: Argument captor?

        verify(notificationDao).save(notification);
    }
}
