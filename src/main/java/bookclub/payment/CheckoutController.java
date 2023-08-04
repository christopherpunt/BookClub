package bookclub.payment;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CheckoutController {

    @Value("${stripe.public.key}")
    private String stripePublicKey;

    @GetMapping("/donate")
    public String donate(Model model){
        model.addAttribute("donateForm", new DonateForm());
        return "pay/donate";
    }

    @PostMapping("/checkout")
    public String checkout(@ModelAttribute @Valid DonateForm donateForm, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "pay/donate";
        }
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("amount", donateForm.getAmount());
        return "pay/checkout";
    }
}
