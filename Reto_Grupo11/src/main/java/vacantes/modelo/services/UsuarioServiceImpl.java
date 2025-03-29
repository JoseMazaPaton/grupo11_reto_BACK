package vacantes.modelo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vacantes.modelo.entities.Usuario;
import vacantes.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepository uRepo;
	

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


}
