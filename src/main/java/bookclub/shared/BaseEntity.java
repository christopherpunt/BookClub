package bookclub.shared;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected LocalDateTime createdDate;

    @Column(nullable = false)
    protected LocalDateTime modifiedDate;

    @Column(nullable = false)
    protected StatusEnum status;

    @PrePersist
    protected void onCreate(){
        createdDate = LocalDateTime.now();
        modifiedDate = LocalDateTime.now();
        status = StatusEnum.CREATED;
    }

    @PreUpdate
    protected void onUpdate(){
        modifiedDate = LocalDateTime.now();
    }

}
