package vacantes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes.modelo.entities.Empresa;
import vacantes.modelo.entities.Usuario;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
	
	public Empresa findByNombreEmpresa (String nombre);
	
	Optional<Empresa> findByUsuario(Usuario usuario);
	
}
