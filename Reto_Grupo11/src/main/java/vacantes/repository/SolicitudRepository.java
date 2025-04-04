package vacantes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes.modelo.entities.Solicitud;
import vacantes.modelo.entities.Usuario;
import vacantes.modelo.entities.Vacante;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

    Solicitud findByVacanteAndUsuario(Vacante vacante, Usuario usuario);
    
}
