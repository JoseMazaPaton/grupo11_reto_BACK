package vacantes.controller;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import vacantes.dto.VacanteRequestDto;
import vacantes.dto.VacanteResponseDto;
import vacantes.modelo.entities.Categoria;
import vacantes.modelo.entities.Empresa;
import vacantes.modelo.entities.EstadoVacante;
import vacantes.modelo.entities.Solicitud;
import vacantes.modelo.entities.Usuario;
import vacantes.modelo.entities.Vacante;
import vacantes.modelo.services.CategoriaServiceImpl;
import vacantes.modelo.services.EmpresaServiceImpl;
import vacantes.modelo.services.SolicitudServiceImpl;
import vacantes.modelo.services.UsuarioServiceImpl;
import vacantes.modelo.services.VacanteServiceImpl;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/vacante")
@Tag(name = "Vacante", description ="Operaciones sobre las vacantes")
public class VacanteController {
	
	@Autowired
	private VacanteServiceImpl vService;
	
	@Autowired
	private UsuarioServiceImpl uService;
	
	@Autowired
	private CategoriaServiceImpl cService;
	
	@Autowired
	private EmpresaServiceImpl eService;
	
	@Autowired
	private SolicitudServiceImpl sService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	@Operation(
	        summary = "Obtener todas las vacantes",
	        description = "Devuelve una lista completa de vacantes disponibles en el sistema."
	    )
	    @ApiResponse(
	        responseCode = "200",
	        description = "Lista de vacantes",
	        content = @Content(
	            mediaType = "application/json",
	            examples = @ExampleObject(
	                name = "Ejemplo de respuesta",
	                value = """
	                [
	                  {
	                    "idVacante": 1,
	                    "nombre": "Desarrollador Backend",
	                    "descripcion": "Responsable de construir APIs REST en Java.",
	                    "fecha": "2025-04-12",
	                    "salario": 2800.0,
	                    "estatus": "CREADA",
	                    "destacado": true,
	                    "imagen": "backend.jpg",
	                    "detalles": "Teletrabajo disponible, contrato indefinido.",
	                    "categoria": "Backend",
	                    "empresa": "Tech Solutions"
	                  }
	                ]
	                """
	            )
	        )
	    )
	@GetMapping("/todas")
	public ResponseEntity<List<VacanteResponseDto>> todasVacantes() {
		
		List<Vacante> vacantes = vService.buscarTodos();
		
		List<VacanteResponseDto> response = vacantes.stream()
				.map(this::convertToDto).toList();
		
		return ResponseEntity.ok(response);
		
	}
	
	@Operation(
	        summary = "Crear una nueva vacante",
	        description = "Crea una vacante asociada a una categoría y empresa existentes."
	    )
	    @ApiResponses({
	        @ApiResponse(responseCode = "201", description = "Vacante creada correctamente"),
	        @ApiResponse(responseCode = "404", description = "Categoría o empresa no encontrada")
	    })
	@PostMapping("/add")
	public ResponseEntity<?> altaVacante (
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
		            description = "Datos para la nueva vacante",
		            required = true,
		            content = @Content(
		                mediaType = "application/json",
		                examples = @ExampleObject(
		                    name = "Ejemplo de alta vacante",
		                    value = """
		                    {
		                      "nombre": "QA Tester",
		                      "descripcion": "Pruebas automáticas de software",
		                      "salario": 2200,
		                      "imagen": "qa.jpg",
		                      "detalles": "Horario flexible, jornada completa",
		                      "nombreCategoria": "Calidad",
		                      "nombreEmpresa": "SoftControl"
		                    }
		                    """
		                )
		            )
		        )
			@RequestBody VacanteRequestDto request){
		
		Categoria categoria = cService.buscarPorNombre(request.getNombreCategoria());
		if(categoria == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay coincidencia con la categoria: " + request.getNombreCategoria());
		}
		  
		Empresa empresa =  eService.buscarPorNombre(request.getNombreEmpresa());
		
		if(empresa == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay coincidencia con la empresa: " + request.getNombreEmpresa());
		}
		
		Vacante vacante = modelMapper.map(request, Vacante.class);
	    vacante.setCategoria(categoria);
	    vacante.setEmpresa(empresa);
	    
	    vacante.setEstatus(EstadoVacante.CREADA);
	    vacante.setDestacado(true);
	    
	    vacante.setFecha(new java.sql.Date(System.currentTimeMillis()));
	    
	    vService.insertUno(vacante);
	    
	    return ResponseEntity.status(HttpStatus.CREATED)
	    		.body("Vacante creada correctamente");
		
	}
	
	@Operation(
	        summary = "Modificar una vacante existente",
	        description = "Permite actualizar los datos de una vacante por su ID."
	    )
	    @ApiResponses({
	        @ApiResponse(responseCode = "200", description = "Vacante actualizada correctamente"),
	        @ApiResponse(responseCode = "404", description = "Vacante, categoría o empresa no encontrada")
	    })
	@PutMapping("/modificar/{idVacante}")
	public ResponseEntity<String> modificarVacante(
									@Parameter(description = "ID de la vacante a modificar", example = "5")
									@PathVariable int idVacante,
	        						@RequestBody VacanteRequestDto request) {

	    
	    Vacante vacante = vService.buscarUno(idVacante);
	    
	    if (vacante == null) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay coincidencia con la vacante: " + request.getNombre());
	    }
	        

	    Categoria categoria = cService.buscarPorNombre(request.getNombreCategoria());
	    if (categoria == null) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay coincidencia con la categoria: " + request.getNombreCategoria());
	    }

	    Empresa empresa = eService.buscarPorNombre(request.getNombreEmpresa());

	    if (empresa == null) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay coincidencia con la empresa: " + request.getNombreEmpresa());
	    }

	   
	    modelMapper.map(request, vacante);
	    vacante.setIdVacante(idVacante); // Por si ModelMapper lo borra (aunque no debería)

	    
	    vacante.setCategoria(categoria);
	    vacante.setEmpresa(empresa);

	   
	    vService.insertUno(vacante);

	    return ResponseEntity.ok("Vacante actualizada con éxito");
	}
	
	@Operation(
	        summary = "Ver detalle de una vacante",
	        description = "Devuelve la información completa de una vacante específica por su ID."
	    )
	    @ApiResponses({
	        @ApiResponse(
	            responseCode = "200",
	            description = "Vacante encontrada",
	            content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = VacanteResponseDto.class),
	                examples = @ExampleObject(
	                    name = "Ejemplo de vacante",
	                    value = """
	                    {
	                      "idVacante": 3,
	                      "nombre": "Diseñador UX",
	                      "descripcion": "Diseño de experiencias digitales atractivas.",
	                      "fecha": "2025-04-10",
	                      "salario": 2300,
	                      "estatus": "CREADA",
	                      "destacado": false,
	                      "imagen": "ux.jpg",
	                      "detalles": "Trabajo híbrido. Formación continua.",
	                      "categoria": "Diseño",
	                      "empresa": "Creative Minds"
	                    }
	                    """
	                )
	            )
	        ),
	        @ApiResponse(responseCode = "404", description = "Vacante no encontrada")
	    })
	@GetMapping("/detalle/{idVacante}")
	public ResponseEntity<?> detalleVacante(@PathVariable int idVacante) {
	    
	    Vacante vacante = vService.buscarUno(idVacante);
	       
	    if (vacante == null) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay coincidencia con la vacante que se quiere ver");
	    }

	    
	    VacanteResponseDto response = modelMapper.map(vacante, VacanteResponseDto.class);

	    
	    response.setCategoria(vacante.getCategoria().getNombre());
	    response.setEmpresa(vacante.getEmpresa().getNombreEmpresa());

	    return ResponseEntity.ok(response);
	}
	
	
	 @Operation(
		        summary = "Eliminar una vacante",
		        description = "Elimina una vacante según su ID."
		    )
		    @ApiResponses({
		        @ApiResponse(responseCode = "200", description = "Vacante eliminada"),
		        @ApiResponse(responseCode = "404", description = "Vacante no encontrada")
		    })
	@DeleteMapping("/eliminar/{idVacante}")
	public ResponseEntity<String> eliminarVacante(
			@Parameter(description = "ID de la vacante a eliminar", example = "6")
			@PathVariable int idVacante) {
	    // Verificamos que exista antes de eliminar
	    Vacante vacante = vService.buscarUno(idVacante);
	    
	    if (vacante == null) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay coincidencia con la vacante que se quiere eliminar");
	    }
	    

	    vService.deleteUno(idVacante);
	    
	    return ResponseEntity.ok("Vacante eliminada con éxito");
	}
	
	 
	 @Operation(
		        summary = "Asignar una vacante a un candidato",
		        description = "Cambia el estado de la vacante a 'CUBIERTA' y adjudica la solicitud correspondiente."
		    )
		    @ApiResponses({
		        @ApiResponse(responseCode = "200", description = "Vacante asignada con éxito"),
		        @ApiResponse(responseCode = "403", description = "No tienes permiso para esta acción"),
		        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
		    })
	@PutMapping("/asignar/{idSolicitud}")
	public ResponseEntity<String> asignarVacante(
			@Parameter(description = "ID de la solicitud que se quiere adjudicar", example = "10")
			@PathVariable int idSolicitud,
			Authentication authentication) {
	    
	    Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();

	    
	    Solicitud solicitud = sService.buscarUno(idSolicitud);
	    
	    if (solicitud == null) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay coincidencia con la solicitud que se quiere gestionar");
	    }
	        

	    Vacante vacante = solicitud.getVacante();

	    // Verificar si la vacante pertenece a la empresa del usuario autenticado
	    if (!vacante.getEmpresa().getUsuario().getEmail().equals(usuarioAutenticado.getEmail())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para asignar esta vacante");
	    }

	    
	    vacante.setEstatus(EstadoVacante.CUBIERTA);

	    // Cambiar estado de la solicitud a adjudicada 
	    solicitud.setEstado(1);

	  
	    vService.insertUno(vacante);
	    sService.insertUno(solicitud);

	    return ResponseEntity.ok("Vacante asignada y solicitud adjudicada con éxito");
	}
	
	
	
	
	private VacanteResponseDto convertToDto(Vacante vacante) {
        VacanteResponseDto dto = modelMapper.map(vacante, VacanteResponseDto.class);
        // Mapeo manual de campos que ModelMapper no puede resolver automáticamente
        dto.setCategoria(vacante.getCategoria().getNombre());
        dto.setEmpresa(vacante.getEmpresa().getNombreEmpresa());
        return dto;
    }

}
