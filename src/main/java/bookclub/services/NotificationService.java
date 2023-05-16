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
    NotificationRepository notificationRepo;

    @Autowired
    EmailService emailService;

    @Autowired
    UserRepository userRepo;

    @Autowired
    BookRepository bookRepo;

    public boolean sendBorrowRequest(String borrowerEmail, Long friendId, Long bookId) {
        Optional<User> borrower = userRepo.findByEmail(borrowerEmail);
        Optional<User> loaner = userRepo.findById(friendId);
        Optional<Book> book = bookRepo.findById(bookId);

        if (borrower.isEmpty() || loaner.isEmpty() || book.isEmpty()) {
            return false;
        }

        Notification notification = new Notification();
        notification.setNotificationType(NotificationType.BORROW_REQUEST);
        notification.setStatus(StatusEnum.UNREAD);
        notification.setReceiver(loaner.get());
        notification.setSender(borrower.get());
        notification.addNotificationData(NotificationData.BOOK_ID, bookId);
        notification.setAction("action");

        emailService.sendBookRequestNotification(borrower.get().getEmail(), loaner.get().getEmail(), bookId);

        notificationRepo.save(notification);
        return true;
    }

    public boolean sendFriendRequest(String username, Long userId) {
        Optional<User> sender = userRepo.findByEmail(username);
        Optional<User> receiver = userRepo.findById(userId);

        if (sender.isEmpty() || receiver.isEmpty()) {
            return false;
        }

        Notification notification = new Notification();
        notification.setNotificationType(NotificationType.FRIEND_REQUEST);
        notification.setStatus(StatusEnum.UNREAD);
        notification.setSender(sender.get());
        notification.setReceiver(receiver.get());

        emailService.sendFriendRequest(sender.get().getEmail(), receiver.get().getEmail());

        notificationRepo.save(notification);
        return true;
    }

    public List<Notification> getNotificationsForUsername(String username) {
        Optional<User> user = userRepo.findByEmail(username);

        if (user.isEmpty()) {
            return new ArrayList<>();
        }

        Optional<List<Notification>> notificationList = notificationRepo.findByReceiver(user.get());
        return notificationList.orElseGet(ArrayList::new);
    }

    public List<Notification> getUncompletedNotificationsForUser(String username){
        List<Notification> notifications = getNotificationsForUsername(username);
        return notifications.stream().filter(n -> n.getStatus().getValue() < StatusEnum.COMPLETED.getValue()).toList();
    }
}
