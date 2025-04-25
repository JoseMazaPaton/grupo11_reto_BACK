package vacantes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes.modelo.entities.Empresa;
import vacantes.modelo.entities.Solicitud;
import vacantes.modelo.entities.Usuario;
import vacantes.modelo.entities.Vacante;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

    Solicitud findByVacanteAndUsuario(Vacante vacante, Usuario usuario);
    
    List<Solicitud> findByUsuario(Usuario usuario); 
    List<Solicitud> findByVacante_Empresa(Empresa empresa);
    List<Solicitud> findByVacante_IdVacante(int idVacante);
    
    
}
