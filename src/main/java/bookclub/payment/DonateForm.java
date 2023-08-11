package bookclub.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DonateForm {

    @NotNull
    @Min(1)
    Integer amount;

    public DonateForm() {
        amount = 0;
    }
}
