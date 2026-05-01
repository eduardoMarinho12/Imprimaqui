package springproject.imprimaqui.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springproject.imprimaqui.domain.order.OrderStatus;
import springproject.imprimaqui.service.OrderService;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String listarPedidos(Model model) {
        model.addAttribute("orders", orderService.listarPedidos());
        model.addAttribute("statuses", OrderStatus.values());
        return "admin-orders";
    }

    @PostMapping("/{id}/status")
    public String atualizarStatus(@PathVariable Long id,
                                  @RequestParam OrderStatus status,
                                  RedirectAttributes redirectAttributes) {
        orderService.atualizarStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Status do pedido atualizado com sucesso.");
        return "redirect:/admin/orders";
    }
}
