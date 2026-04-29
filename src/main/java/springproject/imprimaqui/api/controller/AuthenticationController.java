package springproject.imprimaqui.api.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import springproject.imprimaqui.domain.user.User;
import springproject.imprimaqui.dto.auth.LoginDTO;
import springproject.imprimaqui.dto.auth.RegisterDTO;
import springproject.imprimaqui.dto.user.UserResponseDTO;
import springproject.imprimaqui.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService service;

    public AuthenticationController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public UserResponseDTO registrar(@RequestBody @Valid RegisterDTO dto) {
        User user = service.registrar(dto.getName(), dto.getAge(), dto.getEmail(), dto.getPassword());
        return UserResponseDTO.from(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginDTO dto) {
        User user = service.autenticar(dto.getEmail(), dto.getPassword());
        return "Login realizado com sucesso: " + user.getEmail();
    }
}
