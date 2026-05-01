package springproject.imprimaqui.web.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import springproject.imprimaqui.dto.product.ProductResponseDTO;
import springproject.imprimaqui.service.OrderService;
import springproject.imprimaqui.service.ProductService;
import springproject.imprimaqui.service.UserService;
import springproject.imprimaqui.web.dto.LoginFormDTO;
import springproject.imprimaqui.web.dto.RegisterFormDTO;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class SiteController {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;

    public SiteController(UserService userService,
                          ProductService productService,
                          OrderService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("featuredProducts", listarProdutosDestaque());
        return "home";
    }

    @GetMapping("/")
    public String bair(Model model) {
        model.addAttribute("featuredProducts", listarProdutosDestaque());
        return "home";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout,
                        @RequestParam(required = false) String registered,
                        Model model) {
        model.addAttribute("loginForm", new LoginFormDTO());

        if (error != null) {
            model.addAttribute("errorMessage", "Email ou senha invalidos.");
        }

        if (logout != null) {
            model.addAttribute("successMessage", "Voce saiu da sua conta com sucesso.");
        }

        if (registered != null) {
            model.addAttribute("successMessage", "Cadastro realizado com sucesso. Agora faca seu login.");
        }

        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastrar(Model model) {
        if (!model.containsAttribute("registerForm")) {
            model.addAttribute("registerForm", new RegisterFormDTO());
        }

        return "cadastro";
    }

    @GetMapping("/produtos")
    public String produtos(@RequestParam(required = false) Long productId,
                           @RequestParam(required = false) String zipCode,
                           @RequestParam(required = false, defaultValue = "1") Integer quantity,
                           Model model) {
        List<ProductResponseDTO> products = productService.listarProdutosAtivos();
        ProductResponseDTO selectedProduct = products.isEmpty() ? null : products.get(0);

        if (productId != null) {
            selectedProduct = products.stream()
                    .filter(product -> product.getId().equals(productId))
                    .findFirst()
                    .orElse(selectedProduct);
        }

        model.addAttribute("products", products);
        model.addAttribute("selectedProduct", selectedProduct);
        model.addAttribute("selectedQuantity", Math.max(quantity, 1));

        if (selectedProduct != null) {
            BigDecimal subtotal = selectedProduct.getUnitPrice().multiply(BigDecimal.valueOf(Math.max(quantity, 1)));
            model.addAttribute("subtotalEstimate", subtotal);

            if (zipCode != null && !zipCode.isBlank()) {
                try {
                    BigDecimal shippingEstimate = orderService.calcularFreteEstimado(zipCode);
                    model.addAttribute("zipCode", zipCode);
                    model.addAttribute("shippingEstimate", shippingEstimate);
                    model.addAttribute("totalEstimate", subtotal.add(shippingEstimate));
                } catch (ResponseStatusException ex) {
                    model.addAttribute("zipCode", zipCode);
                    model.addAttribute("errorMessage", ex.getReason());
                }
            }
        }

        return "produtos";
    }

    @PostMapping("/cadastro")
    public String cadastroUsuario(@Valid @ModelAttribute("registerForm") RegisterFormDTO user,
                                  BindingResult result,
                                  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Confira os campos obrigatorios antes de continuar.");
            return "cadastro";
        }

        try {
            userService.registrar(
                    user.getName(),
                    user.getAge(),
                    user.getEmail(),
                    user.getPassword()
            );
            return "redirect:/login?registered";
        } catch (ResponseStatusException ex) {
            model.addAttribute("errorMessage", ex.getReason());
            return "cadastro";
        }
    }

    @GetMapping("/about")
    public String aboutUs() {
        return "aboutus";
    }

    @GetMapping("/contact")
    public String contacts() {
        return "contact";
    }

    private List<ProductResponseDTO> listarProdutosDestaque() {
        return productService.listarProdutosAtivos()
                .stream()
                .limit(4)
                .toList();
    }
}
