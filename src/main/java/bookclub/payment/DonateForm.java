package bookclub.payment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DonateForm {

    @NotNull
    @Min(1)
    Integer amount;

    @NotNull
    @Email
    String email;

    public DonateForm() {
        amount = 0;
        email = "";
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public @NotNull String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }
}
