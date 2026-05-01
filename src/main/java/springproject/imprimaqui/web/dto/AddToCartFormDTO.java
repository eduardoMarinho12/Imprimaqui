package springproject.imprimaqui.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartFormDTO {

    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
