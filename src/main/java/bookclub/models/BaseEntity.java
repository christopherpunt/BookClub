package bookclub.models;

import bookclub.enums.StatusEnum;
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
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    @Column(nullable = false)
    private StatusEnum status;

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
