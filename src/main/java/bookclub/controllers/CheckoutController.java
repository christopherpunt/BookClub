package bookclub.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckoutController {

    @Value("${stripe.public.key}")
    private String stripePublicKey;

    @GetMapping("/donate")
    public String donate(Model model){

        model.addAttribute("stripePublicKey", stripePublicKey);
        return "pay/checkout";
    }
}
