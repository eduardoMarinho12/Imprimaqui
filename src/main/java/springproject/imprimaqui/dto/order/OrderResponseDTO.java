package springproject.imprimaqui.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springproject.imprimaqui.domain.order.CustomerOrder;
import springproject.imprimaqui.domain.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private Long id;
    private Long userId;
    private String userEmail;
    private OrderStatus status;
    private String shippingZipCode;
    private BigDecimal shippingCost;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDTO> items;

    public static OrderResponseDTO from(CustomerOrder order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .userEmail(order.getUser().getEmail())
                .status(order.getStatus())
                .shippingZipCode(order.getShippingZipCode())
                .shippingCost(order.getShippingCost())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .items(order.getItems().stream().map(OrderItemResponseDTO::from).toList())
                .build();
    }
}
