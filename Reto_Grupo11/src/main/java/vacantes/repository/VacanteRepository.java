package vacantes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes.modelo.entities.Empresa;
import vacantes.modelo.entities.EstadoVacante;
import vacantes.modelo.entities.Vacante;

public interface VacanteRepository extends JpaRepository<Vacante, Integer> {
	
	public Vacante findByNombre (String nombre);
	public List<Vacante> findByEmpresa (Empresa empresa);
	public List<Vacante> findByEstatus (EstadoVacante estatus);
}
