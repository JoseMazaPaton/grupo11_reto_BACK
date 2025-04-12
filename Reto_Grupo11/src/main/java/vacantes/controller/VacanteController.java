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
	
	@GetMapping("/todas")
	public ResponseEntity<List<VacanteResponseDto>> todasVacantes() {
		
		List<Vacante> vacantes = vService.buscarTodos();
		
		List<VacanteResponseDto> response = vacantes.stream()
				.map(this::convertToDto).toList();
		
		return ResponseEntity.ok(response);
		
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> altaVacante (@RequestBody VacanteRequestDto request){
		
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
	
	@PutMapping("/modificar/{idVacante}")
	public ResponseEntity<String> modificarVacante(@PathVariable int idVacante,
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
	
	@DeleteMapping("/eliminar/{idVacante}")
	public ResponseEntity<String> eliminarVacante(@PathVariable int idVacante) {
	    // Verificamos que exista antes de eliminar
	    Vacante vacante = vService.buscarUno(idVacante);
	    
	    if (vacante == null) {
	    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay coincidencia con la vacante que se quiere eliminar");
	    }
	    

	    vService.deleteUno(idVacante);
	    
	    return ResponseEntity.ok("Vacante eliminada con éxito");
	}
	
	@PutMapping("/asignar/{idSolicitud}")
	public ResponseEntity<String> asignarVacante(@PathVariable int idSolicitud, Authentication authentication) {
	    
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
