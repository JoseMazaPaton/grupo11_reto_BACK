package vacantes.modelo.services;

import vacantes.dto.SolicitudRequestDto;
import vacantes.modelo.entities.Solicitud;
import vacantes.modelo.entities.Usuario;

public interface UsuarioService extends CrudGenerico<Usuario, String> {

	Usuario buscarPorEmail(String email);
	
	int enviarSolicitud (Solicitud solicitud);
	int enviarSolicitudDto (SolicitudRequestDto dto, int idVacante, String email);
}
