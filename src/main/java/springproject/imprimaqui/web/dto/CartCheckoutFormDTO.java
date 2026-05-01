package springproject.imprimaqui.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartCheckoutFormDTO {

    @NotBlank
    private String shippingZipCode;
}
