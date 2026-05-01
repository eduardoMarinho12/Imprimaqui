package springproject.imprimaqui.web.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springproject.imprimaqui.dto.user.UserResponseDTO;
import springproject.imprimaqui.service.UserService;
import springproject.imprimaqui.web.dto.UserManagementFormDTO;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("users", userService.listarUsuarios());
        return "admin-users";
    }

    @GetMapping("/{id}/edit")
    public String editarUsuario(@PathVariable Long id, Model model) {
        UserResponseDTO user = userService.buscarUsuarioPorId(id);
        model.addAttribute("userId", id);
        model.addAttribute("userForm", new UserManagementFormDTO(
                user.getName(),
                user.getAge(),
                user.getEmail(),
                ""
        ));
        return "admin-user-form";
    }

    @PostMapping("/{id}/edit")
    public String atualizarUsuario(@PathVariable Long id,
                                   @Valid @ModelAttribute("userForm") UserManagementFormDTO form,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("userId", id);
            return "admin-user-form";
        }

        userService.atualizarUsuario(id, form.getName(), form.getAge(), form.getEmail(), form.getPassword());
        redirectAttributes.addFlashAttribute("successMessage", "Usuario atualizado com sucesso.");
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/delete")
    public String deletarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deletarUsuario(id);
        redirectAttributes.addFlashAttribute("successMessage", "Usuario removido com sucesso.");
        return "redirect:/admin/users";
    }
}
