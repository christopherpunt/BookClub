package unitTest;

import bookclub.enums.NotificationData;
import bookclub.enums.NotificationType;
import bookclub.models.Book;
import bookclub.models.Notification;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.NotificationRepository;
import bookclub.repositories.UserRepository;
import bookclub.services.EmailService;
import bookclub.services.NotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import utils.BookTestUtils;
import utils.UserTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotificationServiceTest extends BaseUnitTest{

    @Mock
    private NotificationRepository notificationDao;

    @Mock
    private UserRepository userDao;

    @Mock
    private BookRepository bookDao;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    public void sendBorrowRequestNotificationTest(){
        //arrange
        User borrower = UserTestUtils.createUser("Chris Punt");
        User loaner = UserTestUtils.createUser("Chris2 Punt2");
        Book book = BookTestUtils.createOwnedBook(loaner);

        when(userDao.findByEmail(borrower.getEmail())).thenReturn(Optional.of(borrower));
        when(userDao.findById(loaner.getId())).thenReturn(Optional.of(loaner));
        when(bookDao.findById(book.getId())).thenReturn(Optional.of(book));

        //act
        boolean returnValue = notificationService.sendBorrowRequest(borrower.getEmail(), loaner.getId(), book.getId());

        //assert
        assertTrue(returnValue);

        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationDao).save(notificationCaptor.capture());
        assertNotNull(notificationCaptor.getValue());
        Notification notification = notificationCaptor.getValue();

        assertEquals(borrower, notification.getSender());
        assertEquals(loaner, notification.getReceiver());
        assertEquals(NotificationType.BorrowRequest, notification.getNotificationType());
        assertEquals(book.getId(), notification.getNotificationData().get(NotificationData.BOOK_ID));

        verify(emailService).sendBookRequestNotification(borrower.getEmail(), loaner.getEmail(), book.getId());
    }

    @Test
    public void sendFriendRequestNotificationTest(){
        //arrange
        User sender = UserTestUtils.createUser("Chris Punt");
        User receiver = UserTestUtils.createUser("Sydney Punt");

        when(userDao.findByEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        when(userDao.findById(receiver.getId())).thenReturn(Optional.of(receiver));

        //act
        boolean returnValue = notificationService.sendFriendRequest(sender.getEmail(), receiver.getId());

        //assert
        assertTrue(returnValue);

        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationDao).save(notificationCaptor.capture());
        assertNotNull(notificationCaptor.getValue());
        Notification notification = notificationCaptor.getValue();

        assertEquals(sender, notification.getSender());
        assertEquals(receiver, notification.getReceiver());
        assertEquals(NotificationType.FriendRequest, notification.getNotificationType());

        verify(emailService).sendFriendRequest(sender.getEmail(), receiver.getEmail());
    }
}
