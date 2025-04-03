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

import vacantes.modelo.entities.Solicitud;
import vacantes.modelo.services.SolicitudService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/solicitud")
public class SolicitudController {

	@Autowired
	SolicitudService solicitudService;
	
	//Buscar todas las solicitudes (GET)
	@GetMapping("/all")
	public ResponseEntity<List<Solicitud>> todos() {
	
		return new ResponseEntity<List<Solicitud>>(solicitudService.buscarTodos(), HttpStatus.OK);
	
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
	public ResponseEntity<?> crearSolicitud(@RequestBody Solicitud solicitud) {
	    
		Solicitud nuevaSolicitud = solicitudService.insertUno(solicitud);
	        
	    if (nuevaSolicitud != null) {
	        return new ResponseEntity<>("Solicitud añadida", HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>("La solicitud ya existe", HttpStatus.BAD_REQUEST);
	    }
	
	   
	    }
	    
	    //Actualizar una solicitud (PUT)
	@PutMapping("/edit/{idSolicitud}")
	public ResponseEntity<?> actualizarSolicitud(@PathVariable int idSolicitud, @RequestBody Solicitud solicitudActualizada) {
	    
		Solicitud solicitudExistente = solicitudService.buscarUno(idSolicitud);
		if (solicitudExistente != null) {
			solicitudActualizada.setIdSolicitud(idSolicitud);
			switch(solicitudService.updateUno(solicitudActualizada)) {
				case 1:  return new ResponseEntity<>("Solicitud actualizada correctamente", HttpStatus.OK);
				case 0:  return new ResponseEntity<>("No se encontró la solicitud", HttpStatus.NOT_FOUND);
				default:  return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
			else
				return new ResponseEntity<String>("Esta solicitud no existe", HttpStatus.NOT_FOUND);
	
	    }
}
