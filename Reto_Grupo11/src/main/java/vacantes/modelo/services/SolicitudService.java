package vacantes.modelo.services;

import java.util.List;

import vacantes.modelo.entities.Solicitud;
import vacantes.modelo.entities.Usuario;
import vacantes.modelo.entities.Vacante;

public interface SolicitudService extends CrudGenerico<Solicitud, Integer> {
	
	Solicitud buscarPorVacanteAndUsuario(Vacante vacante, Usuario usuario);
	
	List<Solicitud> buscarPorUsuario(Usuario usuario);

}
