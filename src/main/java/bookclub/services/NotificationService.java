package bookclub.services;

import bookclub.enums.NotificationData;
import bookclub.enums.NotificationStatus;
import bookclub.enums.NotificationType;
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
    UserRepository userDao;

    @Autowired
    BookRepository bookDao;

    public boolean sendBorrowRequest(String username, Long friend, Long bookId) {
        Optional<User> sender = userDao.findByEmail(username);
        Optional<User> receiver = userDao.findById(friend);
        Optional<Book> book = bookDao.findById(bookId);

        if (sender.isEmpty() || receiver.isEmpty() || book.isEmpty()) {
            return false;
        }

        Notification notification = new Notification();
        notification.setNotificationType(NotificationType.BorrowRequest);
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setReceiver(receiver.get());
        notification.setSender(sender.get());
        notification.addNotificationData(NotificationData.BOOK_ID, bookId);
        notification.setAction("action");

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
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setSender(sender.get());
        notification.setReceiver(receiver.get());

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
