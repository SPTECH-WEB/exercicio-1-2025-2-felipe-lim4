package school.sptech.prova_ac1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {



    boolean existsByEmailLikeIgnoreCaseOrCpfLikeIgnoreCase(String email, String cpf);

    List<Usuario> findByDataNascimentoGreaterThan(LocalDate dataNascimento);

    boolean existsByCpfLikeIgnoreCase(String cpf);

    boolean existsByEmailLike(String email);

    boolean existsByEmailLikeIgnoreCase(String email);

}
