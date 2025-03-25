package vacantes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes.modelo.entities.Solicitud;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

}
