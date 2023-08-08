package bookclub.payment;

import bookclub.shared.StatusEnum;
import bookclub.user.User;
import bookclub.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;

@Service
public class PaymentService {

    private Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private DonationRepository donationRepo;

    @Autowired
    private UserRepository userRepo;

    public void handleDonationIntent(String userEmail, String paymentIntentId, Long amount){
        Optional<User> donorOptional = userRepo.findByEmail(userEmail);
        if (donorOptional.isPresent()){
            Donation donation = new Donation();
            donation.setDonor(donorOptional.get());
            donation.setPaymentIntentId(paymentIntentId);
            donation.setAmount(amount);
            donationRepo.save(donation);
        }
    }

    public void handlePaymentCompleted(String userEmail, String paymentIntentId, Long amount){
        Optional<User> donorOptional = userRepo.findByEmail(userEmail);
        if (donorOptional.isEmpty()){
            logger.error("donor not found");
            return;
        }

        Donation donation = donationRepo.findByPaymentIntentId(paymentIntentId);
        if (!Objects.equals(donation.getAmount(), amount)){
            logger.warn(MessageFormat.format("Donation amounts were not the same for donation: " +
                    "{0}, amount from db: {1}, amount from paymentIntent: {2}",
                    donation.getId(), donation.getAmount(), amount));
        }

        donation.setStatus(StatusEnum.COMPLETED);
        donationRepo.save(donation);
    }
}
