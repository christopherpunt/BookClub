package bookclub.services;

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
        Optional<User> user = userDao.findById(friend);
        Optional<Book> book = bookDao.findById(bookId);

        Notification notification = new Notification();
        if (sender.isPresent() && user.isPresent() && book.isPresent()){
            notification.setNotificationType(NotificationType.BorrowRequest);
            notification.setStatus(NotificationStatus.UNREAD);
            notification.setReceiver(user.get());
            notification.setSender(sender.get());
            notification.setAction("action");

            notificationDao.save(notification);
            return true;
        }
        return false;
    }

    public Notification sendFriendRequest(String username, Long userId){
        Optional<User> user = userDao.findByEmail(username);
        Optional<User> friend = userDao.findById(userId);

        Notification notification = new Notification();
        if(user.isPresent() && friend.isPresent()){
            notification.setNotificationType(NotificationType.FriendRequest);
            notification.setStatus(NotificationStatus.UNREAD);
            notification.setSender(user.get());
            notification.setReceiver(friend.get());

            notificationDao.save(notification);
        }

        return notification;
    }

    public List<Notification> getNotificationsForUsername(String username) {
        Optional<User> user = userDao.findByEmail(username);

        if (user.isEmpty()){
            return new ArrayList<>();
        }

        Optional<List<Notification>> notificationList = notificationDao.findByReceiver(user.get());
        return notificationList.orElseGet(ArrayList::new);
    }

    public void completeNotification(Long id) {
        Optional<Notification> notificationOptional = notificationDao.findById(id);

        if (notificationOptional.isEmpty()){
            return;
        }
        Notification notification = notificationOptional.get();

        notification.setStatus(NotificationStatus.COMPLETED);

        //TODO: check notification type and act accordingly

        notificationDao.save(notification);
    }
}
