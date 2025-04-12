package vacantes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes.modelo.entities.Vacante;

public interface VacanteRepository extends JpaRepository<Vacante, Integer> {
	
	public Vacante findByNombre (String nombre);

}
