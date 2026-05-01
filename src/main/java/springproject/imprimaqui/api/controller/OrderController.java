package springproject.imprimaqui.api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springproject.imprimaqui.domain.order.OrderStatus;
import springproject.imprimaqui.dto.order.OrderCreateRequestDTO;
import springproject.imprimaqui.dto.order.OrderResponseDTO;
import springproject.imprimaqui.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/me")
    public List<OrderResponseDTO> listarPedidosDoUsuario(Authentication authentication) {
        return orderService.listarPedidosDoUsuario(authentication.getName());
    }

    @GetMapping
    public List<OrderResponseDTO> listarPedidos() {
        return orderService.listarPedidos();
    }

    @GetMapping("/{id}")
    public OrderResponseDTO buscarPedido(@PathVariable Long id) {
        return orderService.buscarPedido(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderResponseDTO criarPedido(Authentication authentication,
                                        @RequestBody @Valid OrderCreateRequestDTO dto) {
        return orderService.criarPedido(authentication.getName(), dto);
    }

    @PatchMapping("/{id}/status")
    public OrderResponseDTO atualizarStatus(@PathVariable Long id,
                                            @RequestParam OrderStatus status) {
        return orderService.atualizarStatus(id, status);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deletarPedido(@PathVariable Long id) {
        orderService.deletarPedido(id);
    }
}
