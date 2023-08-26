package bookclub.notification;

import bookclub.purge.PurgeableEntity;
import bookclub.shared.BaseEntity;
import bookclub.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@PurgeableEntity(repositoryClass = NotificationRepository.class)
public class Notification extends BaseEntity {
    @ManyToOne
    private User receiver;

    @ManyToOne
    private User sender;

    @Column
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

}
