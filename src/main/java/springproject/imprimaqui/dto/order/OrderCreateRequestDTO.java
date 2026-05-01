package springproject.imprimaqui.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestDTO {

    @NotBlank
    private String shippingZipCode;

    @NotEmpty
    @Valid
    private List<OrderItemRequestDTO> items;
}
