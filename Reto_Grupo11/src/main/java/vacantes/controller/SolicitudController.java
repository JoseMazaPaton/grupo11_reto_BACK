package vacantes.controller;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;

import vacantes.dto.SolicitudRequestDto;
import vacantes.dto.SolicitudResponseDto;
import vacantes.dto.VacanteResponseDto;
import vacantes.modelo.entities.Empresa;
import vacantes.modelo.entities.Solicitud;
import vacantes.modelo.entities.Usuario;
import vacantes.modelo.entities.Vacante;
import vacantes.modelo.services.EmpresaService;
import vacantes.modelo.services.SolicitudService;
import vacantes.modelo.services.UsuarioService;
import vacantes.modelo.services.VacanteService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/solicitud")
@Tag(name = "Solicitud", description ="Operaciones sobre las solicitudes")
public class SolicitudController {

	@Autowired
	private SolicitudService solicitudService;
	
	@Autowired
	private VacanteService vService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	//Buscar todas las solicitudes (GET)
	@Operation(
	        summary = "Obtener todas las solicitudes",
	        description = "Devuelve una lista con todas las solicitudes, incluyendo datos de la vacante y empresa."
	    )
	    @ApiResponse(
	        responseCode = "200",
	        description = "Lista de solicitudes",
	        content = @Content(
	            mediaType = "application/json",
	            examples = @ExampleObject(
	                name = "Ejemplo de respuesta",
	                value = """
	                [
	                  {
	                    "fecha": "2025-04-12",
	                    "archivos": "portafolio.pdf",
	                    "estado": 0,
	                    "curriculum": "cv_john_doe.pdf",
	                    "nombreVacante": "Desarrollador Java",
	                    "imagenVacante": "java.jpg",
	                    "nombreEmpresa": "Tech Solutions"
	                  }
	                ]
	                """
	            )
	        )
	    )
	@GetMapping("/all")
	public ResponseEntity<List<SolicitudResponseDto>> todos() {
	    List<Solicitud> solicitudes = solicitudService.buscarTodos();

	    List<SolicitudResponseDto> respuesta = solicitudes.stream()
	        .map(solicitud -> SolicitudResponseDto.builder()
	            .fecha(solicitud.getFecha())
	            .archivos(solicitud.getArchivo())
	            .estado(solicitud.getEstado())
	            .curriculum(solicitud.getCurriculum())
	            .nombreVacante(solicitud.getVacante().getNombre())
	            .imagenVacante(solicitud.getVacante().getImagen())
	            .nombreEmpresa(solicitud.getVacante().getEmpresa().getNombreEmpresa())
	            .build())
	        .toList();

	    return ResponseEntity.ok(respuesta);
	}
	
	//Detalle de una solicitud (GET)
		@Operation(
		        summary = "Ver detalle de una solicitud",
		        description = "Devuelve la información completa de una solicitud específica por su ID."
		    )
		@ApiResponses({
	        @ApiResponse(
	            responseCode = "200",
	            description = "Solicitud encontrada",
	            content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = SolicitudResponseDto.class),
	                examples = @ExampleObject(
	                    name = "Ejemplo de solicitud",
	                    value = """
						{
						    "fecha": "2025-04-02",
						    "archivos": "archivo1.pdf",
						    "estado": 0,
						    "curriculum": "CV_Cliente1.pdf",
						    "nombreVacante": "Desarrollador Java",
						    "imagenVacante": "imagen1.jpg",
						    "nombreEmpresa": "Tech Solutions"
						}
	                    """
	                )
	            )
	        ),
	        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
	    })
		@GetMapping("/detail/{idSolicitud}")
		public ResponseEntity<?> detalleSolicitud(
				@Parameter(description = "ID de la solicitud", example = "2")
				@PathVariable int idSolicitud) {
			
		    Solicitud solicitud = solicitudService.buscarUno(idSolicitud);
		       
		    if (solicitud == null) {
		    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay coincidencia con la solicitud que se quiere ver");
		    }

		    
		    SolicitudResponseDto response = modelMapper.map(solicitud, SolicitudResponseDto.class);

		    response.setArchivos(solicitud.getArchivo());
		    response.setNombreEmpresa(solicitud.getVacante().getEmpresa().getNombreEmpresa());

		    return ResponseEntity.ok(response);
		}
	
	
	//Eliminar una solicitud (DELETE)
	@Operation(
	        summary = "Eliminar una solicitud",
	        description = "Elimina una solicitud existente según su ID."
	    )
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Solicitud eliminada correctamente"),
	        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
	        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
	    })
	@DeleteMapping("/delete/{idSolicitud}")
	public ResponseEntity<?> eliminarSolicitud(
			@Parameter(description = "ID de la solicitud", example = "5")
			@PathVariable int idSolicitud) {
	    
		switch(solicitudService.deleteUno(idSolicitud)) {
			/*case 1:  return new ResponseEntity<>("Solicitud eliminada correctamente", HttpStatus.OK);
			case 0:  return new ResponseEntity<>("Solicitud NO eliminada. No se encontró la solicitud", HttpStatus.NOT_FOUND);
			default:  return new ResponseEntity<>("Error desconocido", HttpStatus.INTERNAL_SERVER_ERROR);*/
			
			case 1: return ResponseEntity.ok(Map.of("message", "Solicitud eliminada correctamente"));
			case 0: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Solicitud NO eliminada. No se encontró la solicitud"));
			default: return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error desconocido"));
		
       }
	
	}
	    
	//Crear una nueva Solicitud 
	@Operation(
	        summary = "Crear una nueva solicitud",
	        description = "Permite a un usuario autenticado postularse a una vacante"
	    )
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "201", description = "Solicitud creada"),
	        @ApiResponse(responseCode = "400", description = "Solicitud duplicada"),
	        @ApiResponse(responseCode = "409", description = "Vacante no encontrada")
	    })   
	@PostMapping("/add")
	public ResponseEntity<?> crearSolicitud(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
		            description = "Datos para crear la solicitud",
		            required = true,
		            content = @Content(
		                mediaType = "application/json",
		                examples = @ExampleObject(
		                    name = "Ejemplo de solicitud",
		                    value = """
		                    {
		                      "archivo": "portafolio.pdf",
		                      "comentarios": "Tengo experiencia en este tipo de proyectos.",
		                      "curriculum": "cv_john_doe.pdf",
		                      "nombreVacante": "Desarrollador Java"
		                    }
		                    """
		                )
		            )
		        )
			@RequestBody SolicitudRequestDto dto) {
	    // Buscar la vacante asociada por su nombre
	    Vacante vacante = vService.buscarPorNombre(dto.getNombreVacante());
	    if (vacante == null) {
	    	return new ResponseEntity<>("Vacante con nombre: " + dto.getNombreVacante() + " no existe", HttpStatus.CONFLICT);
	    }

	    Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

	    // Crear la solicitud
	    Solicitud solicitud = Solicitud.builder()
	        .archivo(dto.getArchivo())
	        .comentarios(dto.getComentarios())
	        .curriculum(dto.getCurriculum())
	        .fecha(new java.sql.Date(System.currentTimeMillis()))
	        .estado(0) // Estado inicial: 0 = pendiente
	        .vacante(vacante)
	        .usuario(usuario)
	        .build();

	    // Guardar
	    Solicitud nueva = solicitudService.insertUno(solicitud);

	    if (nueva != null) {
	        return new ResponseEntity<>("Solicitud añadida correctamente", HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>("La solicitud ya existe", HttpStatus.BAD_REQUEST);
	    }
	}
	    
	    //Actualizar una solicitud (PUT)
	@Operation(
	        summary = "Actualizar solicitud existente",
	        description = "Permite modificar los datos de una solicitud ya creada"
	    )
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Solicitud actualizada"),
	        @ApiResponse(responseCode = "404", description = "Solicitud o vacante no encontrada"),
	        @ApiResponse(responseCode = "409", description = "Nombre de vacante no válido"),
	        @ApiResponse(responseCode = "500", description = "Error interno")
	    })
	@PutMapping("/edit/{idSolicitud}")
	public ResponseEntity<?> actualizarSolicitud(
			@Parameter(description = "ID de la solicitud a modificar", example = "10")
	        @PathVariable int idSolicitud,
	        @io.swagger.v3.oas.annotations.parameters.RequestBody(
	                description = "Datos actualizados",
	                required = true,
	                content = @Content(
	                    mediaType = "application/json",
	                    examples = @ExampleObject(
	                        name = "Ejemplo de actualización",
	                        value = """
	                        {
	                          "archivo": "nuevo_portafolio.pdf",
	                          "comentarios": "He actualizado mi experiencia.",
	                          "curriculum": "cv_nuevo.pdf",
	                          "nombreVacante": "Desarrollador Frontend"
	                        }
	                        """
	                    )
	                )
	            )
	        @RequestBody SolicitudRequestDto dto) {

	    // Buscar la solicitud existente
	    Solicitud solicitudExistente = solicitudService.buscarUno(idSolicitud);
	    if (solicitudExistente == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("Esta solicitud no existe");
	    }

	    // Actualizamos solo los campos permitidos desde el DTO
	    solicitudExistente.setArchivo(dto.getArchivo());
	    solicitudExistente.setComentarios(dto.getComentarios());
	    solicitudExistente.setCurriculum(dto.getCurriculum());

	    // Si también quieres permitir cambiar de vacante:
	    Vacante nuevaVacante = vService.buscarPorNombre(dto.getNombreVacante());
	    
	    if (nuevaVacante == null) {
	    	return new ResponseEntity<>("Vacante con nombre: " + dto.getNombreVacante() + " no existe", HttpStatus.CONFLICT);
	    }
	        
	    solicitudExistente.setVacante(nuevaVacante);

	    // Guardar los cambios
	    int resultado = solicitudService.updateUno(solicitudExistente);

	    return switch (resultado) {
	        case 1 -> ResponseEntity.ok("Solicitud actualizada correctamente");
	        case 0 -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la solicitud");
	        default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar");
	    };
	}
	
	
	@GetMapping("/missolicitudes")
	public ResponseEntity<?> obtenerMisSolicitudes() {
	    // 1. Obtener email del usuario autenticado desde el token JWT
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String email = auth.getName();

	    // 2. Buscar el usuario autenticado
	    Usuario usuario = usuarioService.buscarPorEmail(email);
	    if (usuario == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	            .body(Map.of("message", "❌ Usuario no autorizado"));
	    }

	    // 3. Buscar solicitudes asociadas al usuario
	    List<Solicitud> solicitudes = solicitudService.buscarPorUsuario(usuario);

	    // 4. Si no hay solicitudes, devolver mensaje en formato Map
	    if (solicitudes.isEmpty()) {
	        return ResponseEntity.ok(Map.of("message", "📭 No tienes solicitudes registradas"));
	    }

	    // 5. Mapear a DTOs
	    List<SolicitudResponseDto> respuesta = solicitudes.stream()
	        .map(solicitud -> SolicitudResponseDto.builder()
	            .fecha(solicitud.getFecha())
	            .archivos(solicitud.getArchivo())
	            .estado(solicitud.getEstado())
	            .curriculum(solicitud.getCurriculum())
	            .nombreVacante(solicitud.getVacante().getNombre())
	            .imagenVacante(solicitud.getVacante().getImagen())
	            .nombreEmpresa(solicitud.getVacante().getEmpresa().getNombreEmpresa())
	            .emailUsuario(solicitud.getUsuario().getEmail())
	            .nombreUsuario(solicitud.getUsuario().getNombre())
	            .idSolicitud(solicitud.getIdSolicitud())
	            .build())
	        .toList();

	    return ResponseEntity.ok(respuesta);
	}
	
	
	
	@Operation(
		    summary = "Solicitudes recibidas por la empresa autenticada",
		    description = "Devuelve todas las solicitudes para las vacantes creadas por la empresa logueada"
		)
		@GetMapping("/empresa")
		public ResponseEntity<?> solicitudesPorEmpresa() {
		    // Obtener email desde el token JWT
		    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    String email = auth.getName();

		    Empresa empresa = empresaService.buscarPorEmail(email);
		    if (empresa == null) {
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
		            .body(Map.of("message", "❌ Empresa no autorizada"));
		    }

		    List<Solicitud> solicitudes = solicitudService.buscarPorEmpresa(empresa);

		    if (solicitudes.isEmpty()) {
		        return ResponseEntity.ok(Map.of("message", "📭 No hay solicitudes registradas"));
		    }

		    List<SolicitudResponseDto> respuesta = solicitudes.stream()
		    	    .filter(s -> s.getVacante() != null && s.getUsuario() != null)
		    	    .map(s -> SolicitudResponseDto.builder()
		    	        .fecha(s.getFecha())
		    	        .archivos(s.getArchivo())
		    	        .estado(s.getEstado())
		    	        .curriculum(s.getCurriculum())
		    	        .nombreVacante(s.getVacante().getNombre())
		    	        .imagenVacante(s.getVacante().getImagen())
		    	        .nombreEmpresa(empresa.getNombreEmpresa())
		    	        .emailUsuario(s.getUsuario().getEmail())
		    	        .nombreUsuario(s.getUsuario().getNombre() + " " + s.getUsuario().getApellidos())
		    	        .build())
		    	    .toList();
		    return ResponseEntity.ok(respuesta);
		}
	
	
	@PutMapping("/adjudicar/{idSolicitud}")
	@Operation(
	    summary = "Adjudicar solicitud a un candidato",
	    description = "Marca una solicitud como adjudicada (estado = 1). Solo una por vacante.",
	    parameters = @Parameter(name = "idSolicitud", description = "ID de la solicitud a adjudicar", example = "3"),
	    responses = {
	        @ApiResponse(responseCode = "200", description = "Solicitud adjudicada correctamente"),
	        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
	        @ApiResponse(responseCode = "400", description = "Ya hay una adjudicación previa para esta vacante")
	    }
	)
	public ResponseEntity<?> adjudicarSolicitud(@PathVariable int idSolicitud) {
	    Solicitud solicitud = solicitudService.buscarUno(idSolicitud);
	    if (solicitud == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitud no encontrada");
	    }

	    // Verificar si ya hay una adjudicación para esta vacante
	    boolean yaAdjudicada = solicitudService
	        .buscarPorVacante(solicitud.getVacante())
	        .stream()
	        .anyMatch(s -> s.getEstado() == 1);

	    if (yaAdjudicada) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            .body(Map.of("message", "⚠️ Ya hay una solicitud adjudicada para esta vacante"));
	    }

	    solicitud.setEstado(1);
	    int resultado = solicitudService.updateUno(solicitud);

	    if (resultado == 1) {
	        return ResponseEntity.ok(Map.of("message", "✅ Solicitud adjudicada correctamente"));
	    } else {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(Map.of("message", "❌ Error al adjudicar solicitud"));
	    }
	}
	
}
