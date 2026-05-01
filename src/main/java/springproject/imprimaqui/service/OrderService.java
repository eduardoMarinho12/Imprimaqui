package springproject.imprimaqui.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import springproject.imprimaqui.domain.order.CustomerOrder;
import springproject.imprimaqui.domain.order.CustomerOrderRepository;
import springproject.imprimaqui.domain.order.OrderItem;
import springproject.imprimaqui.domain.order.OrderStatus;
import springproject.imprimaqui.domain.product.Product;
import springproject.imprimaqui.domain.user.User;
import springproject.imprimaqui.domain.user.UserRepository;
import springproject.imprimaqui.dto.order.OrderCreateRequestDTO;
import springproject.imprimaqui.dto.order.OrderResponseDTO;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final CustomerOrderRepository orderRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

    public OrderService(CustomerOrderRepository orderRepository,
                        ProductService productService,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    public List<OrderResponseDTO> listarPedidos() {
        return orderRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(OrderResponseDTO::from)
                .toList();
    }

    public List<OrderResponseDTO> listarPedidosDoUsuario(String email) {
        User user = buscarUsuarioPorEmail(email);
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(OrderResponseDTO::from)
                .toList();
    }

    public OrderResponseDTO buscarPedido(Long id) {
        return OrderResponseDTO.from(buscarEntidade(id));
    }

    public BigDecimal calcularFreteEstimado(String zipCode) {
        return calcularFrete(zipCode);
    }

    @Transactional
    public OrderResponseDTO criarPedido(String userEmail, OrderCreateRequestDTO dto) {
        User user = buscarUsuarioPorEmail(userEmail);
        BigDecimal shippingCost = calcularFrete(dto.getShippingZipCode());

        CustomerOrder order = CustomerOrder.builder()
                .user(user)
                .status(OrderStatus.CRIADO)
                .shippingZipCode(dto.getShippingZipCode())
                .shippingCost(shippingCost)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal subtotal = BigDecimal.ZERO;

        for (var itemDto : dto.getItems()) {
            Product product = productService.buscarEntidade(itemDto.getProductId());
            BigDecimal unitPrice = product.getUnitPrice();
            BigDecimal itemSubtotal = unitPrice.multiply(BigDecimal.valueOf(itemDto.getQuantity()));

            OrderItem item = OrderItem.builder()
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .unitPrice(unitPrice)
                    .subtotal(itemSubtotal)
                    .build();

            order.addItem(item);
            subtotal = subtotal.add(itemSubtotal);
        }

        order.setTotalAmount(subtotal.add(shippingCost));
        return OrderResponseDTO.from(orderRepository.save(order));
    }

    public OrderResponseDTO atualizarStatus(Long id, OrderStatus status) {
        CustomerOrder order = buscarEntidade(id);
        order.setStatus(status);
        return OrderResponseDTO.from(orderRepository.save(order));
    }

    public void deletarPedido(Long id) {
        orderRepository.delete(buscarEntidade(id));
    }

    private CustomerOrder buscarEntidade(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }

    private User buscarUsuarioPorEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    private BigDecimal calcularFrete(String zipCode) {
        String digits = zipCode.replaceAll("\\D", "");

        if (digits.length() != 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP inválido");
        }

        int firstDigit = Integer.parseInt(digits.substring(0, 1));

        if (firstDigit <= 2) {
            return BigDecimal.valueOf(8.50);
        }

        if (firstDigit <= 5) {
            return BigDecimal.valueOf(13.90);
        }

        return BigDecimal.ZERO;
    }
}
