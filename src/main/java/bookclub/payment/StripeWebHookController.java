package bookclub.payment;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StripeWebHookController {

    private Logger logger = LoggerFactory.getLogger(StripeWebHookController.class);

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    @PostMapping("/stripe/event")
    public String handleStripeEvent(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader){
            Event event = null;
            if(stripeWebhookSecret != null && sigHeader != null) {
                // Only verify the event if you have an endpoint secret defined.
                // Otherwise, use the basic event deserialized with GSON.
                try {
                    event = Webhook.constructEvent(
                            payload, sigHeader, stripeWebhookSecret
                    );
                } catch (SignatureVerificationException e) {
                    // Invalid signature
                    logger.warn("⚠️  Webhook error while validating signature.");
                    return "";
                }
            }
            // Deserialize the nested object inside the event
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;
            if (dataObjectDeserializer.getObject().isPresent()) {
                stripeObject = dataObjectDeserializer.getObject().get();
            } else {
                // Deserialization failed, probably due to an API version mismatch.
                // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
                // instructions on how to handle this case, or return an error here.
            }
            // Handle the event
            switch (event.getType()) {
                case "payment_intent.succeeded":
                    PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                    // Then define and call a method to handle the successful payment intent.
                    if (paymentIntent != null) {
                        handlePaymentIntentSucceeded(paymentIntent);
                    }
                    break;
                default:
                    logger.info("Unhandled event type: " + event.getType());
                    break;
            }
            return "";
    }

    private void handlePaymentIntentSucceeded(PaymentIntent paymentIntent){
        logger.info("Payment for " + paymentIntent.getAmount() + " succeeded.");

        //TODO: handle payment
    }
}
