package springproject.imprimaqui.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import springproject.imprimaqui.service.CartService;

@ControllerAdvice(basePackages = "springproject.imprimaqui.web.controller")
public class WebModelAttributesConfig {

    private final CartService cartService;

    public WebModelAttributesConfig(CartService cartService) {
        this.cartService = cartService;
    }

    @ModelAttribute("cartItemCount")
    public Integer cartItemCount(HttpSession session) {
        return cartService.getCartView(session).getItemCount();
    }
}
