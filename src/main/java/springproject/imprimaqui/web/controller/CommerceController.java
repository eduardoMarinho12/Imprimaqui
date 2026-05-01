package springproject.imprimaqui.web.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import springproject.imprimaqui.dto.order.OrderResponseDTO;
import springproject.imprimaqui.service.CartService;
import springproject.imprimaqui.service.OrderService;
import springproject.imprimaqui.web.dto.AddToCartFormDTO;
import springproject.imprimaqui.web.dto.CartCheckoutFormDTO;
import springproject.imprimaqui.web.dto.CartViewDTO;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class CommerceController {

    private final CartService cartService;
    private final OrderService orderService;

    public CommerceController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @PostMapping("/carrinho/adicionar")
    public String adicionarAoCarrinho(@Valid @ModelAttribute AddToCartFormDTO form,
                                      BindingResult result,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Não foi possível adicionar o item ao carrinho.");
            return "redirect:/produtos";
        }

        cartService.addItem(session, form.getProductId(), form.getQuantity());
        redirectAttributes.addFlashAttribute("successMessage", "Produto adicionado ao carrinho com sucesso.");
        return "redirect:/carrinho";
    }

    @GetMapping("/carrinho")
    public String carrinho(Model model, HttpSession session) {
        model.addAttribute("cart", cartService.getCartView(session));
        model.addAttribute("checkoutForm", new CartCheckoutFormDTO());
        return "carrinho";
    }

    @PostMapping("/carrinho/{productId}/atualizar")
    public String atualizarCarrinho(@PathVariable Long productId,
                                    @RequestParam Integer quantity,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        cartService.updateItem(session, productId, quantity);
        redirectAttributes.addFlashAttribute("successMessage", "Carrinho atualizado.");
        return "redirect:/carrinho";
    }

    @PostMapping("/carrinho/{productId}/remover")
    public String removerDoCarrinho(@PathVariable Long productId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        cartService.removeItem(session, productId);
        redirectAttributes.addFlashAttribute("successMessage", "Item removido do carrinho.");
        return "redirect:/carrinho";
    }

    @GetMapping("/checkout")
    public String checkout(@RequestParam(required = false) String zipCode,
                           Model model,
                           HttpSession session) {
        CartViewDTO cart = cartService.getCartView(session);
        if (cart.getItems().isEmpty()) {
            return "redirect:/carrinho";
        }

        model.addAttribute("cart", cart);
        model.addAttribute("checkoutForm", new CartCheckoutFormDTO(zipCode));

        if (zipCode != null && !zipCode.isBlank()) {
            try {
                BigDecimal shipping = orderService.calcularFreteEstimado(zipCode);
                model.addAttribute("shippingEstimate", shipping);
                model.addAttribute("grandTotal", cart.getSubtotal().add(shipping));
            } catch (ResponseStatusException ex) {
                model.addAttribute("errorMessage", ex.getReason());
            }
        }

        return "checkout";
    }

    @PostMapping("/checkout")
    public String finalizarPedido(@Valid @ModelAttribute("checkoutForm") CartCheckoutFormDTO form,
                                  BindingResult result,
                                  Authentication authentication,
                                  HttpSession session,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        CartViewDTO cart = cartService.getCartView(session);
        if (cart.getItems().isEmpty()) {
            return "redirect:/carrinho";
        }

        if (result.hasErrors()) {
            model.addAttribute("cart", cart);
            model.addAttribute("errorMessage", "Informe um CEP válido para continuar.");
            return "checkout";
        }

        try {
            OrderResponseDTO order = orderService.criarPedido(
                    authentication.getName(),
                    cartService.toOrderRequest(session, form.getShippingZipCode())
            );
            cartService.clear(session);
            redirectAttributes.addFlashAttribute("successMessage", "Pedido #" + order.getId() + " criado com sucesso.");
            return "redirect:/meus-pedidos";
        } catch (ResponseStatusException ex) {
            model.addAttribute("cart", cart);
            model.addAttribute("errorMessage", ex.getReason());
            return "checkout";
        }
    }

    @GetMapping("/meus-pedidos")
    public String meusPedidos(Authentication authentication, Model model) {
        List<OrderResponseDTO> orders = orderService.listarPedidosDoUsuario(authentication.getName());
        model.addAttribute("orders", orders);
        return "meus-pedidos";
    }
}
