package vacantes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import vacantes.dto.SolicitudRequestDto;
import vacantes.dto.SolicitudResponseDto;
import vacantes.modelo.entities.Solicitud;
import vacantes.modelo.entities.Usuario;
import vacantes.modelo.entities.Vacante;
import vacantes.modelo.services.SolicitudService;
import vacantes.modelo.services.VacanteService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/solicitud")
public class SolicitudController {

	@Autowired
	private SolicitudService solicitudService;
	
	@Autowired
	private VacanteService vService;
	
	
	//Buscar todas las solicitudes (GET)
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
	
	//Eliminar una solicitud (DELETE)
	@DeleteMapping("/delete/{idSolicitud}")
	public ResponseEntity<?> eliminarSolicitud(@PathVariable int idSolicitud) {
	    
		switch(solicitudService.deleteUno(idSolicitud)) {
			case 1:  return new ResponseEntity<>("Solicitud eliminada correctamente", HttpStatus.OK);
			case 0:  return new ResponseEntity<>("Solicitud NO eliminada. No se encontró la solicitud", HttpStatus.NOT_FOUND);
			default:  return new ResponseEntity<>("Error desconocido", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}
	    
	//Crear una nueva Solicitud 
	   
	@PostMapping("/add")
	public ResponseEntity<?> crearSolicitud(@RequestBody SolicitudRequestDto dto) {
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
	@PutMapping("/edit/{idSolicitud}")
	public ResponseEntity<?> actualizarSolicitud(
	        @PathVariable int idSolicitud,
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
}
