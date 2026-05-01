package springproject.imprimaqui.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springproject.imprimaqui.domain.product.PaperSize;
import springproject.imprimaqui.domain.product.PrintType;
import springproject.imprimaqui.domain.product.Product;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private PrintType printType;
    private PaperSize paperSize;
    private BigDecimal unitPrice;
    private boolean active;

    public static ProductResponseDTO from(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .printType(product.getPrintType())
                .paperSize(product.getPaperSize())
                .unitPrice(product.getUnitPrice())
                .active(product.isActive())
                .build();
    }
}
