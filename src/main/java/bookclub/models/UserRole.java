package bookclub.models;

import bookclub.enums.UserRoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserRole extends BaseEntity {

    UserRole() {}
    public UserRole(UserRoleEnum userRoleEnum){
        this.userRole = userRoleEnum;
    }

    @Column
    @Enumerated(EnumType.STRING)
    private UserRoleEnum userRole;
}
