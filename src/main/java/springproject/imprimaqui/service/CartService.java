package springproject.imprimaqui.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import springproject.imprimaqui.domain.product.Product;
import springproject.imprimaqui.dto.order.OrderCreateRequestDTO;
import springproject.imprimaqui.dto.order.OrderItemRequestDTO;
import springproject.imprimaqui.web.dto.CartItemViewDTO;
import springproject.imprimaqui.web.dto.CartViewDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    private static final String CART_SESSION_KEY = "cart";

    private final ProductService productService;

    public CartService(ProductService productService) {
        this.productService = productService;
    }

    public void addItem(HttpSession session, Long productId, Integer quantity) {
        CartSession cartSession = getOrCreateCart(session);
        cartSession.items.merge(productId, quantity, Integer::sum);
        session.setAttribute(CART_SESSION_KEY, cartSession);
    }

    public void updateItem(HttpSession session, Long productId, Integer quantity) {
        CartSession cartSession = getOrCreateCart(session);

        if (quantity == null || quantity <= 0) {
            cartSession.items.remove(productId);
        } else {
            cartSession.items.put(productId, quantity);
        }

        session.setAttribute(CART_SESSION_KEY, cartSession);
    }

    public void removeItem(HttpSession session, Long productId) {
        CartSession cartSession = getOrCreateCart(session);
        cartSession.items.remove(productId);
        session.setAttribute(CART_SESSION_KEY, cartSession);
    }

    public void clear(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    public CartViewDTO getCartView(HttpSession session) {
        CartSession cartSession = getOrCreateCart(session);
        List<CartItemViewDTO> items = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        int count = 0;

        for (Map.Entry<Long, Integer> entry : cartSession.items.entrySet()) {
            Product product = productService.buscarEntidade(entry.getKey());
            int quantity = entry.getValue();
            BigDecimal itemSubtotal = product.getUnitPrice().multiply(BigDecimal.valueOf(quantity));

            items.add(CartItemViewDTO.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .description(product.getDescription())
                    .printType(product.getPrintType().name())
                    .paperSize(product.getPaperSize().name())
                    .unitPrice(product.getUnitPrice())
                    .quantity(quantity)
                    .subtotal(itemSubtotal)
                    .build());

            subtotal = subtotal.add(itemSubtotal);
            count += quantity;
        }

        return CartViewDTO.builder()
                .items(items)
                .itemCount(count)
                .subtotal(subtotal)
                .build();
    }

    public boolean isEmpty(HttpSession session) {
        return getCartView(session).getItems().isEmpty();
    }

    public OrderCreateRequestDTO toOrderRequest(HttpSession session, String shippingZipCode) {
        CartViewDTO cart = getCartView(session);

        if (cart.getItems().isEmpty()) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Seu carrinho está vazio.");
        }

        List<OrderItemRequestDTO> items = cart.getItems().stream()
                .map(item -> new OrderItemRequestDTO(item.getProductId(), item.getQuantity()))
                .toList();

        return OrderCreateRequestDTO.builder()
                .shippingZipCode(shippingZipCode)
                .items(items)
                .build();
    }

    private CartSession getOrCreateCart(HttpSession session) {
        CartSession cartSession = (CartSession) session.getAttribute(CART_SESSION_KEY);

        if (cartSession == null) {
            cartSession = new CartSession();
            session.setAttribute(CART_SESSION_KEY, cartSession);
        }

        return cartSession;
    }

    private static class CartSession implements Serializable {
        private final Map<Long, Integer> items = new LinkedHashMap<>();
    }
}
