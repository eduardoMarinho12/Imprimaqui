package springproject.imprimaqui.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import springproject.imprimaqui.domain.user.User;
import springproject.imprimaqui.domain.user.UserRepository;
import springproject.imprimaqui.dto.user.UserResponseDTO;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public User registrar(String name, Integer age, String email, String password) {
        if (repository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ja cadastrado");
        }

        User user = new User();
        user.setName(name);
        user.setAge(age);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));

        return repository.save(user);
    }

    public User autenticar(String email, String password) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha invalida");
        }

        return user;
    }

    public List<UserResponseDTO> listarUsuarios() {
        return repository.findAll()
                .stream()
                .map(UserResponseDTO::from)
                .toList();
    }

    public UserResponseDTO buscarUsuarioPorId(Long id) {
        return UserResponseDTO.from(buscarEntidadePorId(id));
    }

    public UserResponseDTO atualizarUsuario(Long id, String name, Integer age, String email, String password) {
        User user = buscarEntidadePorId(id);

        repository.findByEmail(email)
                .filter(existing -> existing.getId() != id)
                .ifPresent(existing -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ja cadastrado");
                });

        user.setName(name);
        user.setAge(age);
        user.setEmail(email);

        if (password != null && !password.isBlank()) {
            user.setPassword(encoder.encode(password));
        }

        return UserResponseDTO.from(repository.save(user));
    }

    public void deletarUsuario(Long id) {
        User user = buscarEntidadePorId(id);
        repository.delete(user);
    }

    public User buscarEntidadePorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build();
    }
}
