package vacantes.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import vacantes.dto.EmpresaRequestDto;
import vacantes.dto.EmpresaResponseDto;
import vacantes.dto.UsuarioResponseDto;
import vacantes.modelo.entities.Empresa;
import vacantes.modelo.entities.Usuario;
import vacantes.modelo.services.EmpresaService;
import vacantes.modelo.services.UsuarioService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/empresa/")
@Tag(name = "Empresa", description = "API para las operaciones de empresa")
public class EmpresaController {
    
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private UsuarioService usuarioService;
    
	@Autowired
	private ModelMapper modelMapper;

	/***** CRUD *****/
    
	@Operation(
		    summary = "Obtener todos las empresas",
		    description = "Retorna una lista con todas los empresas registradas en la base de datos",
		    responses = {
				@ApiResponse(
				        responseCode = "200",
				        description = "OK. Responde con la lista de empresas obtenida correctamente"
				    )}
	) 
    @GetMapping("all")
    public ResponseEntity<List<EmpresaResponseDto>> listarEmpresas() {
		
		List<Empresa> empresas = empresaService.buscarTodos();
		
		List<EmpresaResponseDto> response = empresas.stream()
				.map(empresa -> EmpresaResponseDto.builder()
						.idEmpresa(empresa.getIdEmpresa())
						.cif(empresa.getCif())
						.nombreEmpresa(empresa.getNombreEmpresa())
						.direccionFiscal(empresa.getDireccionFiscal())
						.pais(empresa.getPais())
						.email(empresa.getUsuario().getEmail())
						.build())
				.toList();
		
        return ResponseEntity.ok(response);

    }

	@Operation(
		    summary = "Obtener un empresa por idEmpresa",
		    description = "Busca y retorna una Empresa a partir de la idEmpresa proporcionado en la URL",
		    parameters = {
		    		@Parameter(
		    			    name = "idEmpresa",
		    			    description = "idEmpresa de la Empresa a buscar. Debe existir en la base de datos",
		    			    required = true,
		    			    example = "1"
		    			)
		    },
		    responses = {
		    	    @ApiResponse(
		    		        responseCode = "200",
		    		        description = "OK. Empresa encontrada y devuelve la Empresa"
		    		    ),
		    		    @ApiResponse(
		    		        responseCode = "404",
		    		        description = "Not Found. La empresa no existe en la base de datos"
		    		    )
		    })
    @GetMapping("detail/{id}")
    public ResponseEntity<?> obtenerEmpresa(@PathVariable int id) {
		
        Empresa empresa = empresaService.buscarUno(id);
        if (empresa == null) {
			return new ResponseEntity<String>("Esta empresa no existe", HttpStatus.NOT_FOUND);
        }
        
        EmpresaResponseDto response = modelMapper.map(empresa, EmpresaResponseDto.class);
        response.setEmail(empresa.getUsuario().getEmail());
        
		return new ResponseEntity<EmpresaResponseDto>(response, HttpStatus.OK);
 
    }

	/******** METODO SIN USO, ALTA EMPRESA SE HACE EN ADMIN ***********/
	/*
	@Operation(
		    summary = "Dar de alta una empresa",
		    description = "Crea una nueva empresa.",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "Datos para registrar una nueva empresa",
		        content = @Content(
		            schema = @Schema(implementation = Empresa.class),
		            examples = {
		                @ExampleObject(
		                    name = "Ejemplo alta empresa",
		                    value = """
			    	            {
			    	              "cif": "A12345678",
			    	              "nombreEmpresa": "Tech Solutions",
			    	              "direccionFiscal": "Calle Tecnología, 123",
			    	              "pais": "España",
			    	              "usuario": {
			    	                "email": "cliente1@correo.com"
			    	              }
			    	            }
		                    """
		                )
		            }
		        )
		    ),
		    responses = {
		    	    @ApiResponse(
		    		        responseCode = "201",
		    		        description = "Created. Empresa añadida correctamente"
		    		    )
		    }
		)
	@Transactional 
    @PostMapping("/add")
    public ResponseEntity<Empresa> crearEmpresa(@RequestBody Empresa empresa) {
        Empresa nuevaEmpresa = empresaService.insertUno(empresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEmpresa);
    }
	*/
	@Operation(
		    summary = "Modificar una empresa existente",
		    description = "Edita los datos de una empresa por su idEmpresa. El idEmpresa no se puede cambiar. Retorna mensaje, en funcion de valor entero de respuesta",
		    parameters = {
		    		@Parameter(
		    			    name = "idEmpresa",
		    			    description = "idEmpresa de la empresa que se desea modificar",
		    			    example = "2"
		    			)
		    },
		    requestBody = 	@io.swagger.v3.oas.annotations.parameters.RequestBody(
				    description = "Objeto Empresa con los datos actualizados (sin cambiar el idEmpresa)",
				    required = true,
				    content = @Content(
				        schema = @Schema(implementation = EmpresaRequestDto.class),
				        examples = @ExampleObject(
				            name = "Ejemplo de actualización de una Empresa",
				            value = """
			    	            {
			    	              "cif": "B87654321",
			    	              "nombreEmpresa": "Marketing Creativo",
			    	              "direccionFiscal": "Av. Publicidad, 456",
			    	              "pais": "Portugal",
			    	              "email": "cliente1@correo.com"
			    	            }
				            """
				        )
				    )
				),
		    responses = {
				    @ApiResponse(responseCode = "200", description = "OK. Empresa actualizada correctamente"),
				    @ApiResponse(responseCode = "404", description = "Not Found. Error: No se encontró la empresa"),
		    }
		)
    @PutMapping("edit/{id}")
    public ResponseEntity<EmpresaResponseDto> actualizarEmpresa(@PathVariable int id, @RequestBody EmpresaRequestDto empresaRequest) {
        
        Empresa empresa = empresaService.buscarUno(id);
         
        if (empresa != null) {
              
            String email = empresaRequest.getEmail();
            
            Usuario usuario = usuarioService.buscarPorEmail(email);
 
            if (usuario != null) {
            	
        	    // Convertimos el DTO a entidad Empresa por el metodo updateUno
        		Empresa empresaActualizada = Empresa.builder()
        				.idEmpresa(id)
        				.cif(empresaRequest.getCif())
        				.nombreEmpresa(empresaRequest.getNombreEmpresa())
        				.direccionFiscal(empresaRequest.getDireccionFiscal())
        				.pais(empresaRequest.getPais())
        				.usuario(usuario)
        				.build();

                int result = empresaService.updateUno(empresaActualizada);
                
                if (result == 1) {
                    EmpresaResponseDto responseDto = EmpresaResponseDto.builder()
                            .idEmpresa(empresaActualizada.getIdEmpresa())
                            .cif(empresaActualizada.getCif())
                            .nombreEmpresa(empresaActualizada.getNombreEmpresa())
                            .direccionFiscal(empresaActualizada.getDireccionFiscal())
                            .pais(empresaActualizada.getPais())
                            .email(empresaActualizada.getUsuario().getEmail())
                            .build();
     	
                    return ResponseEntity.ok(responseDto); 
                } else {
                    return ResponseEntity.notFound().build(); 
                }
            } else {
              
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        
        return ResponseEntity.notFound().build();
    }

	@Operation(
		    summary = "Eliminar una empresa",
		    description = "Elimina una empresa existente del sistema identificada por su idEmpresa. Retorna mensaje, en funcion de valor entero de respuesta",
		    parameters = {
		    		@Parameter(
		    			    name = "idEmpresa",
		    			    description = "idEmpresa de la empresa que se desea modificar",
		    			    example = "1"
		    			)
		    },
		    responses = {
				    @ApiResponse(responseCode = "200", description = "OK. Empresa eliminada correctamente"),
				    @ApiResponse(responseCode = "404", description = "Not Found. No se encontró la empresa")		
		    }
		)
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> eliminarEmpresa(@PathVariable int id) {
        if (empresaService.deleteUno(id) == 1) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.notFound().build(); 
    }
}
