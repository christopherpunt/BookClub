package bookclub.payment;

import bookclub.shared.BaseEntity;
import bookclub.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Donation extends BaseEntity {

    @ManyToOne
    private User donor;

    @Column
    private String paymentIntentId;

    @Column
    private Long amount;
}
