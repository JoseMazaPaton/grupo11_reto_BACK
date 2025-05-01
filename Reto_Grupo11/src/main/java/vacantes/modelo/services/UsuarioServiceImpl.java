package vacantes.modelo.services;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vacantes.dto.SolicitudRequestDto;
import vacantes.modelo.entities.Solicitud;
import vacantes.modelo.entities.Usuario;
import vacantes.modelo.entities.Vacante;
import vacantes.repository.SolicitudRepository;
import vacantes.repository.UsuarioRepository;
import vacantes.repository.VacanteRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepository uRepo;
	
	@Autowired
	private SolicitudRepository sRepo;
	@Autowired
	private VacanteRepository vRepo;

	/**** CRUD ****/
	@Override
	public Usuario buscarUno(String email) {
		return uRepo.findById(email).orElse(null);
	}

	@Override
	public List<Usuario> buscarTodos() {
		return uRepo.findAll();
	}

	@Override
	public Usuario insertUno(Usuario usuario) {
		try {
			if (uRepo.existsById(usuario.getEmail())) {
				return null; // Me aseguro
				// Si lo encuentra devuelve nulo para no darlo de alta.
			}
			else 
				return uRepo.save(usuario);
			
		} catch (Exception e) {
			e.printStackTrace(); 
			return null;
		}
	}

	@Override
	public int updateUno(Usuario usuario) {
	    try {
	        if (uRepo.existsById(usuario.getEmail())) {
	            
	            uRepo.save(usuario);
	            return 1;
	        } else {
	            return 0; 
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1; 
	    }	
	}

	@Override
	public int deleteUno(String email) {
	    try {
			if (uRepo.existsById(email)) {
				uRepo.deleteById(email); 
				return 1; // Si lo encuenta, y lo borra, devuelvo 1.
			}
			else 
				return 0; // Si no existe, devuelvo 0
				
		} catch (Exception e) {
			e.printStackTrace(); // para las pruebas hago un syso de todo lo ocurrido.
			return -1; // Si se casca, devuelvo -1
		}
	}

	@Override
	public Usuario buscarPorEmail(String email) {
		
		return uRepo.findByEmail(email); 
	}

	@Override
	public int enviarSolicitud(Solicitud solicitud) {
		try {
			Usuario usuario = uRepo.findById(solicitud.getUsuario().getEmail()).orElse(null);
			Vacante vacante = vRepo.findById(solicitud.getVacante().getIdVacante()).orElse(null);
	
	        if (usuario == null || vacante == null) {
	            return 0; // Si no existen, devuelvo 0
	        }
	        
	        if (sRepo.findByVacanteAndUsuario(vacante, usuario) != null) { // Comprobamos si ya existe solicitud de ese usuario a esa vacante
	            return 2; // Si ya existe devuelvo 2
	        }
	        
	        // Completamos la solicitud
	        solicitud.setFecha(new Date(System.currentTimeMillis()));
	        solicitud.setEstado(0);
	        solicitud.setUsuario(usuario);
	        solicitud.setVacante(vacante);
	        
	        sRepo.save(solicitud);
	        
	        return 1; // Si envia solicitud correctamente.
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1;
	    }
	}

	@Override
	public int enviarSolicitudDto(SolicitudRequestDto dto, int idVacante, String email) {
		
		try {
			Usuario usuario = uRepo.findById(email).orElse(null);
			Vacante vacante = vRepo.findById(idVacante).orElse(null);
	
	        if (usuario == null || vacante == null) {
	            return 0; // Si no existen, devuelvo 0
	        }
	        
	        if (sRepo.findByVacanteAndUsuario(vacante, usuario) != null) { // Comprobamos si ya existe solicitud de ese usuario a esa vacante
	            return 2; // Si ya existe devuelvo 2
	        }
	        
	        // Completamos la solicitud
		    Solicitud solicitud = Solicitud.builder()
			        .usuario(usuario)
			        .vacante(vacante)
			        .archivo(dto.getArchivo())
			        .comentarios(dto.getComentarios())
			        .curriculum(dto.getCurriculum())
			        .estado(0) // pendiente
			        .fecha(new Date(System.currentTimeMillis()))
			        .build();

			    sRepo.save(solicitud);
			    return 1;
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1;
	    }
	}
}


