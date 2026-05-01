package springproject.imprimaqui.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDTO {

    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
