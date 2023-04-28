package bookclub.services;

import bookclub.enums.NotificationData;
import bookclub.enums.StatusEnum;
import bookclub.models.Notification;
import bookclub.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationCompletionService {

    @Autowired
    NotificationRepository notificationDao;

    @Autowired
    BookService bookService;

    @Autowired
    FriendService friendService;

    public void completeNotification(Long id) {
        Optional<Notification> notificationOptional = notificationDao.findById(id);
        boolean handled = false;

        if (notificationOptional.isEmpty()) {
            return;
        }
        Notification notification = notificationOptional.get();

        notification.setStatus(StatusEnum.COMPLETED);

        switch (notification.getNotificationType()) {
            case BorrowRequest -> {
                bookService.completeBorrowRequest(
                        Long.valueOf((Integer)notification.getNotificationData().get(NotificationData.BOOK_ID.name())),
                        notification.getReceiver(),
                        notification.getSender());
                handled = true;
            }
            case FriendRequest -> {
                friendService.completeFriendship(notification.getSender().getEmail(), notification.getReceiver());
                handled = true;
            }
        }

        if (handled) {
            notificationDao.save(notification);
        }
    }
}
