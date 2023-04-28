package bookclub.models;

import bookclub.converters.NotificationDataConverter;
import bookclub.enums.NotificationData;
import bookclub.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity
public class Notification extends BaseEntity {
    @ManyToOne
    private User receiver;

    @ManyToOne
    private User sender;

    @Column
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Convert(converter = NotificationDataConverter.class)
    @Column
    private Map<NotificationData, Object> notificationData;

    @Column
    private String action;

    public void addNotificationData(NotificationData key, Object value){
        if (notificationData == null){
            notificationData = new HashMap<>();
        }
        notificationData.put(key, value);
    }
}
