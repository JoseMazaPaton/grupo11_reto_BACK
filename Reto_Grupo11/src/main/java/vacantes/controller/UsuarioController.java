package vacantes.controller;


import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import vacantes.modelo.entities.Solicitud;
import vacantes.modelo.entities.Usuario;
import vacantes.modelo.services.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;


@RestController
@CrossOrigin(origins = "*")

@RequestMapping("/usuario")

@Tag(name = "Usuario", description ="Operaciones sobre los usuarios")
public class UsuarioController {
	
	@Autowired
	UsuarioService usuarioService;
	
	@Operation(
		    summary = "Postular a Vacante pasandole una Solicitud",
		    description = "Retorna mensaje, en funcion de valor entero de respuesta",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "El objeto solicitud que contiene los datos necesarios para postularse a una vacante",
		        content = @Content(
		            schema = @Schema(implementation = Solicitud.class),
		            examples = {
		                @ExampleObject(
		                    name = "Ejemplo de solicitud",
		                    value = """
							{
							  "usuario": { "email": "cliente1@correo.com" },
							  "vacante": { "idVacante": 3 },
							  "archivo": "archivo3.pdf",
							  "comentarios": "Interesado en esta vacante",
							  "curriculum": "cv_cliente3.pdf"
							}
		                    """
		                )
		            }
		        )
		    ),
		    responses = {
		    		@ApiResponse(responseCode = "200", description = "OK. Solicitud enviada correctamente"),
		    		@ApiResponse(responseCode = "404", description = "Not_Found. Error: Usuario o vacante no encontrados."),
		    		@ApiResponse(responseCode = "400", description = "Bad_Request. Error: Ya has postulado a esta vacante."),
		    		@ApiResponse(responseCode = "500", description = "Internal_Error. Error Desconocido o problema en la BBDD")
		    }
		)
	@Transactional
    @PostMapping("/solicitud/vacante")
    public ResponseEntity<?> postularVacante(@RequestBody Solicitud solicitud) {
    	Integer respuesta = usuarioService.enviarSolicitud(solicitud);
    	
		switch(respuesta) {
			case 1:  return new ResponseEntity<>("Solicitud enviada correctamente", HttpStatus.OK);
			case 0:  return new ResponseEntity<>("Error: Usuario o vacante no encontrados.", HttpStatus.NOT_FOUND);
			case 2: return new ResponseEntity<>("Error: Ya has postulado a esta vacante.", HttpStatus.BAD_REQUEST);
			case -1: return new ResponseEntity<>("Esto es un problema de la base de datos, llame a servicio Tecnico", HttpStatus.INTERNAL_SERVER_ERROR);
			default:  return new ResponseEntity<>("Error desconocido", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// POSTMAN: localhost:8445/usuario/solicitud/vacante
    }
	
	
	/***** CRUD *****/
    

	@Operation(
		    summary = "Obtener todos los usuarios",
		    description = "Retorna una lista con todos los usuarios registrados en la base de datos",
		    responses = {
				@ApiResponse(
				        responseCode = "200",
				        description = "OK. Responde con la lista de usuarios obtenida correctamente"
				    )}
	)    
	@GetMapping("/all")
	public ResponseEntity<List<Usuario>> todos() {

		return new ResponseEntity<List<Usuario>>(usuarioService.buscarTodos(), HttpStatus.OK);
		
		// POSTMAN: localhost:8445/usuario/all
	}
	

	@Operation(
		    summary = "Obtener un usuario por email",
		    description = "Busca y retorna un usuario a partir del email proporcionado en la URL",
		    parameters = {
		    		@Parameter(
		    			    name = "email",
		    			    description = "Email del usuario a buscar. Debe existir en la base de datos",
		    			    required = true,
		    			    example = "cliente1@correo.com"
		    			)
		    },
		    responses = {
		    	    @ApiResponse(
		    		        responseCode = "200",
		    		        description = "OK. Usuario encontrado y devuelve el usuario"
		    		    ),
		    		    @ApiResponse(
		    		        responseCode = "404",
		    		        description = "Not Found. El usuario no existe en la base de datos"
		    		    )
		    })
	@GetMapping("/detail/{email}")
	public ResponseEntity<?> uno(@PathVariable String email) {
		Usuario usuario = usuarioService.buscarUno(email);
		if (usuario != null)
			return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
		else
			return new ResponseEntity<String>("Este usuario no existe", HttpStatus.NOT_FOUND);
		
		// POSTMAN: localhost:8445/usuario/detail/cliente1@correo.com
	}

	

	@Operation(
		    summary = "Modificar un usuario existente",
		    description = "Edita los datos de un usuario identificado por su email. El email no se puede cambiar. Retorna mensaje, en funcion de valor entero de respuesta",
		    parameters = {
		    		@Parameter(
		    			    name = "email",
		    			    description = "Email del usuario que se desea modificar",
		    			    example = "cliente1@correo.com"
		    			)
		    },
		    requestBody = 	@io.swagger.v3.oas.annotations.parameters.RequestBody(
				    description = "Objeto Usuario con los datos actualizados (sin cambiar el email)",
				    required = true,
				    content = @Content(
				        schema = @Schema(implementation = Usuario.class),
				        examples = @ExampleObject(
				            name = "Ejemplo de actualización de un usuario",
				            value = """
				            {
				              "nombre": "Cliente",
				              "apellidos": "Premium",
				              "password": "cliente123",
				              "enabled": 1,
				              "fechaRegistro": "2025-03-01",
				              "rol": "CLIENTE"
				            }
				            """
				        )
				    )
				),
		    responses = {
				    @ApiResponse(responseCode = "200", description = "OK. Usuario actualizado correctamente"),
				    @ApiResponse(responseCode = "404", description = "Not Found. El usuario no existe"),
				    @ApiResponse(responseCode = "400", description = "Bad Request. Problema en la base de datos"),
				    @ApiResponse(responseCode = "500", description = "Internal Server Error. Error desconocido") 		
		    }
		)
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


			

	@Operation(
		    summary = "Eliminar un usuario",
		    description = "Elimina un usuario existente del sistema identificado por su email. Retorna mensaje, en funcion de valor entero de respuesta",
		    parameters = {
		    		@Parameter(
		    			    name = "email",
		    			    description = "Email del usuario que se desea eliminar",
		    			    example = "nuevo@correo.com"
		    			)
		    },
		    responses = {
				    @ApiResponse(responseCode = "200", description = "OK. Usuario eliminado correctamente"),
				    @ApiResponse(responseCode = "404", description = "Not Found. El usuario no existe"),
				    @ApiResponse(responseCode = "400", description = "Bad Request. Problema en la base de datos"),
				    @ApiResponse(responseCode = "500", description = "Internal Server Error. Error desconocido")		
		    }
		)
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
	
	// NO HACE FALTA YA TENEMOS EL REGISTRO PARA ESTO. TENER EN CUENTA
	/*
	
	@PostMapping("/add")
	public ResponseEntity<?> alta(@RequestBody Usuario usuario) {

		if (usuarioService.buscarPorEmail(usuario.getEmail()) != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Ese email ya esta en uso, usa otro");
		}
		
		usuario.setRol(Rol.CLIENTE);
		usuario.setFechaRegistro(LocalDate.now());
		usuario.setEnabled(1);
		
	    Usuario nuevoUsuario = usuarioService.insertUno(usuario);

	    if (nuevoUsuario != null) {
	        return new ResponseEntity<>("Usuario añadido correctamente", HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>("El usuario ya existe o hubo un error", HttpStatus.BAD_REQUEST);
	    }
		
		// POSTMAN: localhost:8445/usuario/add
				
	}
	*/

}
