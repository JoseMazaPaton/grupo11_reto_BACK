package vacantes.controller;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ResponseEntity<List<Empresa>> listarEmpresas() {
		return new ResponseEntity<List<Empresa>>(empresaService.buscarTodos(), HttpStatus.OK);

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
		    		        description = "Not Found. La categoria no existe en la base de datos"
		    		    )
		    })
    @GetMapping("detail/{id}")
    public ResponseEntity<Empresa> obtenerEmpresa(@PathVariable int id) {
        Empresa empresa = empresaService.buscarUno(id);
        if (empresa != null) {
            return ResponseEntity.ok(empresa); 
        }
        return ResponseEntity.notFound().build(); 
    }

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
	
	@Operation(
		    summary = "Modificar una empresa existente",
		    description = "Edita los datos de una empresa por su idEmpresa. El idEmpresa no se puede cambiar. Retorna mensaje, en funcion de valor entero de respuesta",
		    parameters = {
		    		@Parameter(
		    			    name = "idEmpresa",
		    			    description = "idEmpresa de la empresa que se desea modificar",
		    			    example = "1"
		    			)
		    },
		    requestBody = 	@io.swagger.v3.oas.annotations.parameters.RequestBody(
				    description = "Objeto Empresa con los datos actualizados (sin cambiar el idEmpresa)",
				    required = true,
				    content = @Content(
				        schema = @Schema(implementation = Empresa.class),
				        examples = @ExampleObject(
				            name = "Ejemplo de actualización de una Empresa",
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
				    )
				),
		    responses = {
				    @ApiResponse(responseCode = "200", description = "OK. Empresa actualizada correctamente"),
				    @ApiResponse(responseCode = "404", description = "Not Found. Error: No se encontró la empresa"),
		    }
		)
    @PutMapping("edit/{id}")
    public ResponseEntity<Empresa> actualizarEmpresa(@PathVariable int id, @RequestBody Empresa empresaDetalles) {
        
        Empresa empresa = empresaService.buscarUno(id);
        if (empresa != null) {
            
            empresa.setCif(empresaDetalles.getCif());
            empresa.setNombreEmpresa(empresaDetalles.getNombreEmpresa());
            empresa.setDireccionFiscal(empresaDetalles.getDireccionFiscal());
            empresa.setPais(empresaDetalles.getPais());
            
            String email = empresaDetalles.getUsuario().getEmail();
            
            Usuario usuario = usuarioService.buscarPorEmail(email);
            
            if (usuario != null) {

                empresa.setUsuario(usuario);

                int result = empresaService.updateUno(empresa);
                
                if (result == 1) {
                    return ResponseEntity.ok(empresa); 
                } else {
                    return ResponseEntity.notFound().build(); 
                }
            } else {
              
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
