package springproject.imprimaqui.web.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class CartItemViewDTO {
    Long productId;
    String productName;
    String description;
    String printType;
    String paperSize;
    BigDecimal unitPrice;
    Integer quantity;
    BigDecimal subtotal;
}
