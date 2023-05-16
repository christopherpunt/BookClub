package bookclub.models;

import bookclub.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserRole extends BaseEntity {

    UserRole(User user, UserRoleEnum role){
        this.user = user;
        this.userRole = role;
    }

    @ManyToOne
    private User user;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRoleEnum userRole;
}
