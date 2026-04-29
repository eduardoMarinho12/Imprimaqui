package springproject.imprimaqui.api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springproject.imprimaqui.dto.user.UserResponseDTO;
import springproject.imprimaqui.dto.user.UserUpdateDTO;
import springproject.imprimaqui.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDTO> listarUsuarios() {
        return userService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public UserResponseDTO buscarUsuario(@PathVariable Long id) {
        return userService.buscarUsuarioPorId(id);
    }

    @PutMapping("/{id}")
    public UserResponseDTO atualizarUsuario(@PathVariable Long id,
                                            @RequestBody @Valid UserUpdateDTO dto) {
        return userService.atualizarUsuario(
                id,
                dto.getName(),
                dto.getAge(),
                dto.getEmail(),
                dto.getPassword()
        );
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deletarUsuario(@PathVariable Long id) {
        userService.deletarUsuario(id);
    }
}
