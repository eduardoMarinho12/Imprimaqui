package springproject.imprimaqui.web.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder
public class CartViewDTO {
    List<CartItemViewDTO> items;
    Integer itemCount;
    BigDecimal subtotal;
}
