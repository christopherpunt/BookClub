package bookclub.models;

import bookclub.converters.NotificationDataConverter;
import bookclub.enums.NotificationData;
import bookclub.enums.NotificationStatus;
import bookclub.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

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

    @Convert(converter = NotificationDataConverter.class)
    @Column
    private Map<String, Object> notificationData;

    @Column
    private String action;

    public void addNotificationData(NotificationData key, Object value){
        notificationData.put(key.getKey(), value);
    }
}
