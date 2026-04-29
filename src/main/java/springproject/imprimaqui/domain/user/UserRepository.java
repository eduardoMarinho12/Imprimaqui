package springproject.imprimaqui.domain.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    // Mostrar todos os Usuarios
    @Override
    public List<User> findAll();

    public Optional<User> findById(Long id);

    // Remover usuario
    public void delete(User user);

    // Criar/Alterar usuario
    public User save(User user);

    public Optional<User> findByEmail(String email);

}
