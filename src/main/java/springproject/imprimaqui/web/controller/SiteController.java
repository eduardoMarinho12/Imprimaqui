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
import springproject.imprimaqui.dto.auth.LoginFormDTO;
import springproject.imprimaqui.dto.auth.RegisterDTO;
import springproject.imprimaqui.service.UserService;

@Controller
public class SiteController {

    private final UserService userService;

    public SiteController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/")
    public String bair() {
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
            model.addAttribute("registerForm", new RegisterDTO());
        }

        return "cadastro";
    }

    @GetMapping("/produtos")
    public String produtos() {
        return "produtos";
    }

    @PostMapping("/cadastro")
    public String cadastroUsuario(@Valid @ModelAttribute("registerForm") RegisterDTO user,
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
}
