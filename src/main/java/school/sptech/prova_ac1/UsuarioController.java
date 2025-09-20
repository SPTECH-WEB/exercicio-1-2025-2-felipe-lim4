package school.sptech.prova_ac1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepository repository;

    @GetMapping
    public ResponseEntity<List<Usuario>> buscarTodos() {
        List<Usuario> usuarios = repository.findAll();
        if(usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {

        boolean userExists = repository.existsByEmailLikeIgnoreCaseOrCpfLikeIgnoreCase(usuario.getEmail(), usuario.getCpf());
        boolean userExistsCpf = repository.existsByCpfLikeIgnoreCase(usuario.getCpf());
        boolean userExistseEmail = repository.existsByEmailLike(usuario.getEmail());
        if(userExists || userExistsCpf || userExistseEmail) {

            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return  ResponseEntity.status(HttpStatus.CREATED).body(repository.save(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {

        Optional<Usuario> response = repository.findById(id);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {

        Optional<Usuario> response = repository.findById(id);

        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("/filtro-data")
    public ResponseEntity<List<Usuario>> buscarPorDataNascimento(@RequestParam LocalDate nascimento) {

        List<Usuario> usuarios = repository.findByDataNascimentoGreaterThan(nascimento);

        if(usuarios.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok().body(usuarios);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable
            Integer id,
            @RequestBody
            Usuario usuario
    ) {

        Optional<Usuario> response = repository.findById(id);
        if(response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        boolean cpfExists = repository.existsByCpfLikeIgnoreCase(usuario.getCpf());
        boolean emailExists = repository.existsByEmailLikeIgnoreCase(usuario.getEmail());

        boolean hasSameCpf = response.get().getCpf().equals(usuario.getCpf());
        boolean hasSameEmail = response.get().getEmail().equals(usuario.getEmail());

        if((cpfExists && !hasSameCpf) || (emailExists && !hasSameEmail)){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        usuario.setId(id);
        return ResponseEntity.ok(repository.save(usuario));
    }

}
