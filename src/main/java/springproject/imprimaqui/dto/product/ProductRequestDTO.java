package springproject.imprimaqui.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springproject.imprimaqui.domain.product.PaperSize;
import springproject.imprimaqui.domain.product.PrintType;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private PrintType printType;

    @NotNull
    private PaperSize paperSize;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal unitPrice;

    private boolean active;
}
