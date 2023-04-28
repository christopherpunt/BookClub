package bookclub.services;

import bookclub.enums.NotificationData;
import bookclub.enums.NotificationType;
import bookclub.enums.StatusEnum;
import bookclub.models.Book;
import bookclub.models.Notification;
import bookclub.models.User;
import bookclub.repositories.BookRepository;
import bookclub.repositories.NotificationRepository;
import bookclub.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationDao;

    @Autowired
    EmailService emailService;

    @Autowired
    UserRepository userDao;

    @Autowired
    BookRepository bookDao;

    public boolean sendBorrowRequest(String borrowerEmail, Long friendId, Long bookId) {
        Optional<User> borrower = userDao.findByEmail(borrowerEmail);
        Optional<User> loaner = userDao.findById(friendId);
        Optional<Book> book = bookDao.findById(bookId);

        if (borrower.isEmpty() || loaner.isEmpty() || book.isEmpty()) {
            return false;
        }

        Notification notification = new Notification();
        notification.setNotificationType(NotificationType.BorrowRequest);
        notification.setStatus(StatusEnum.UNREAD);
        notification.setReceiver(loaner.get());
        notification.setSender(borrower.get());
        notification.addNotificationData(NotificationData.BOOK_ID, bookId);
        notification.setAction("action");

        emailService.sendBookRequestNotification(borrower.get().getEmail(), loaner.get().getEmail(), bookId);

        notificationDao.save(notification);
        return true;
    }

    public boolean sendFriendRequest(String username, Long userId) {
        Optional<User> sender = userDao.findByEmail(username);
        Optional<User> receiver = userDao.findById(userId);

        if (sender.isEmpty() || receiver.isEmpty()) {
            return false;
        }

        Notification notification = new Notification();
        notification.setNotificationType(NotificationType.FriendRequest);
        notification.setStatus(StatusEnum.UNREAD);
        notification.setSender(sender.get());
        notification.setReceiver(receiver.get());

        emailService.sendFriendRequest(sender.get().getEmail(), receiver.get().getEmail());

        notificationDao.save(notification);
        return true;
    }

    public List<Notification> getNotificationsForUsername(String username) {
        Optional<User> user = userDao.findByEmail(username);

        if (user.isEmpty()) {
            return new ArrayList<>();
        }

        Optional<List<Notification>> notificationList = notificationDao.findByReceiver(user.get());
        return notificationList.orElseGet(ArrayList::new);
    }
}
