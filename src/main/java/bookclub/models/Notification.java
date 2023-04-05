package bookclub.models;

import bookclub.enums.NotificationStatus;
import bookclub.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User receiver;

    @ManyToOne
    private User sender;

    @Column
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @Column
    private String notificationData;

    @Column
    private String action;

}
