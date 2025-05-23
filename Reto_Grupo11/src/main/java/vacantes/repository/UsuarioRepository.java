package vacantes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes.modelo.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
	
	 Usuario findByEmail(String email);


}
