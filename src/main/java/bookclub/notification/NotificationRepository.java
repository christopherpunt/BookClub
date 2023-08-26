package bookclub.notification;

import bookclub.shared.PurgeableRepository;
import bookclub.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends PurgeableRepository<Notification> {
    Optional<List<Notification>> findByReceiver(User receiver);
}
