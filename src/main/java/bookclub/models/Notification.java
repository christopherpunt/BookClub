package bookclub.models;

import bookclub.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

}
