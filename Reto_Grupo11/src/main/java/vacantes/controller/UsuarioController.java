package vacantes.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vacantes.modelo.entities.Rol;
import vacantes.modelo.entities.Solicitud;
import vacantes.modelo.entities.Usuario;
import vacantes.modelo.services.UsuarioService;

@RestController
@CrossOrigin(origins = "*")

@RequestMapping("/usuario")

public class UsuarioController {
	
	@Autowired
	UsuarioService usuarioService;
	
    @PostMapping("/solicitud/vacante")
    public ResponseEntity<?> postularVacante(@RequestBody Solicitud solicitud) {
    	Integer respuesta = usuarioService.enviarSolicitud(solicitud);
    	
		switch(respuesta) {
			case 1:  return new ResponseEntity<>("Solicitud enviada correctamente", HttpStatus.OK);
			case 0:  return new ResponseEntity<>("Error: Usuario o vacante no encontrados.", HttpStatus.NOT_FOUND);
			case 2: return new ResponseEntity<>("Error: Ya has postulado a esta vacante.", HttpStatus.BAD_REQUEST);
			case -1: return new ResponseEntity<>("Esto es un problema de la base de datos, llame a servicio Tecnico", HttpStatus.BAD_REQUEST);
			default:  return new ResponseEntity<>("Error desconocido", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		
		// POSTMAN: localhost:8445/usuario/solicitud/vacante

    }
	
	
	/***** CRUD *****/
	@GetMapping("/all")
	public ResponseEntity<List<Usuario>> todos() {

		return new ResponseEntity<List<Usuario>>(usuarioService.buscarTodos(), HttpStatus.OK);
		
		// POSTMAN: localhost:8445/usuario/all
	}
	
	@GetMapping("/detail/{email}")
	public ResponseEntity<?> uno(@PathVariable String email) {
		Usuario usuario = usuarioService.buscarUno(email);
		if (usuario != null)
			return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
		else
			return new ResponseEntity<String>("Este usuario no existe", HttpStatus.NOT_FOUND);
		
		// POSTMAN: localhost:8445/usuario/detail/cliente1@correo.com
	}

	@PutMapping("/edit/{email}")
	public ResponseEntity<?> modificar(@PathVariable String email, @RequestBody Usuario usuarioActualizado) {
		Usuario usuarioExistente = usuarioService.buscarUno(email);
		if (usuarioExistente != null) {
			usuarioActualizado.setEmail(email); // Asegurar el email no cambia.
			switch(usuarioService.updateUno(usuarioActualizado)) {
				case 1:  return new ResponseEntity<>("Usuario actualizado correctamente", HttpStatus.OK);
				case 0:  return new ResponseEntity<>("Usuario no existe", HttpStatus.NOT_FOUND);
				case -1: return new ResponseEntity<>("Esto es un problema de la base de datos, llame a servicio Tecnico", HttpStatus.BAD_REQUEST);
				default:  return new ResponseEntity<>("Error desconocido", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}	
		else
			return new ResponseEntity<String>("Este usuario no existe", HttpStatus.NOT_FOUND);
		
		// POSTMAN: localhost:8445/usuario/edit/cliente1@correo.com
				
	}


	@PostMapping("/add")
	public ResponseEntity<?> alta(@RequestBody Usuario usuario) {

		usuario.setRol(Rol.ADMON);
		
	    Usuario nuevoUsuario = usuarioService.insertUno(usuario);

	    if (nuevoUsuario != null) {
	        return new ResponseEntity<>("Usuario añadido correctamente", HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>("El usuario ya existe o hubo un error", HttpStatus.BAD_REQUEST);
	    }
		
		// POSTMAN: localhost:8445/usuario/add
				
	}
			
		 

	@DeleteMapping("/delete/{email}")
	public ResponseEntity<?> eliminar(@PathVariable String email) {
		
		switch(usuarioService.deleteUno(email)) {
			case 1:  return new ResponseEntity<>("Usuario eliminado correctamente", HttpStatus.OK);
			case 0:  return new ResponseEntity<>("Usuario no existe", HttpStatus.NOT_FOUND);
			case -1: return new ResponseEntity<>("Esto es un problema de la base de datos, llame a servicio Tecnico", HttpStatus.BAD_REQUEST);
			default:  return new ResponseEntity<>("Error desconocido", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// POSTMAN: localhost:8445/usuario/delete/nuevo@correo.com
	}

}
