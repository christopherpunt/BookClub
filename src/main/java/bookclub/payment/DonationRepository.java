package bookclub.payment;

import bookclub.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    Optional<List<Donation>> findByDonor(User donor);
    Donation findByPaymentIntentId(String paymentIntentId);

}
