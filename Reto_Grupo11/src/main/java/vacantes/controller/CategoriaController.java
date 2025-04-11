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
import vacantes.dto.AltaEmpresaRequestDto;
import vacantes.dto.AltaEmpresaResponseDto;
import vacantes.modelo.entities.Categoria;
import vacantes.modelo.entities.Usuario;
import vacantes.modelo.services.CategoriaService;


@RestController
@RequestMapping("/categoria")
@CrossOrigin(origins = "*")
@Tag(name = "Categoria", description = "Gestiones relacionadas con Categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    
	/***** CRUD *****/
    
	@Operation(
		    summary = "Obtener todos las categorias",
		    description = "Retorna una lista con todas los categorias registradas en la base de datos",
		    responses = {
				@ApiResponse(
				        responseCode = "200",
				        description = "OK. Responde con la lista de categorias obtenida correctamente"
				    )}
	) 
    // Obtener todas las categorías (GET)
    @GetMapping("/all")
    public ResponseEntity<List<Categoria>> listarCategorias() {
		return new ResponseEntity<List<Categoria>>(categoriaService.buscarTodos(), HttpStatus.OK);
		
		// POSTMAN: localhost:8445/categoria/all
    }

	@Operation(
		    summary = "Obtener un categoria por idCategoria",
		    description = "Busca y retorna una categoria a partir de la idCategoria proporcionado en la URL",
		    parameters = {
		    		@Parameter(
		    			    name = "idCategoria",
		    			    description = "idCategoria de la categoria a buscar. Debe existir en la base de datos",
		    			    required = true,
		    			    example = "1"
		    			)
		    },
		    responses = {
		    	    @ApiResponse(
		    		        responseCode = "200",
		    		        description = "OK. Categoria encontrada y devuelve la categoria"
		    		    ),
		    		    @ApiResponse(
		    		        responseCode = "404",
		    		        description = "Not Found. La categoria no existe en la base de datos"
		    		    )
		    })
    // Obtener una categoría por ID (GET)
    @GetMapping("/detail/{idCategoria}")
    public ResponseEntity<?> buscarCategoria(@PathVariable int idCategoria) {
        Categoria categoria = categoriaService.buscarUno(idCategoria);
		if (categoria != null)
			return new ResponseEntity<Categoria>(categoria, HttpStatus.OK);
		else
			return new ResponseEntity<String>("Este categoria no existe", HttpStatus.NOT_FOUND);
		
		// POSTMAN: localhost:8445/categoria/detail/1  
    }
   
	@Operation(
		    summary = "Dar de alta una categoria",
		    description = "Crea una nueva categoria.",
		    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		        description = "Datos para registrar una nueva categoria",
		        content = @Content(
		            schema = @Schema(implementation = Categoria.class),
		            examples = {
		                @ExampleObject(
		                    name = "Ejemplo alta categoria",
		                    value = """
							      {
    		       		              "nombre": "Finanzas",
		                    		  "descripcion": "Oportunidades en el área de contabilidad, auditoría y análisis financiero"
		                    	}
		                    """
		                )
		            }
		        )
		    ),
		    responses = {
		    	    @ApiResponse(
		    		        responseCode = "201",
		    		        description = "Created. Categoria añadida correctamente"
		    		    ),
		    		    @ApiResponse(
		    		        responseCode = "400",
		    		        description = "Bad Request. La categoria ya existe o hubo un error"
		    		    )
		    }
		)
    // Crear una nueva categoría (POST)
	@Transactional 
    @PostMapping("/add")
    public ResponseEntity<?> crearCategoria(@RequestBody Categoria categoria) {
        Categoria nuevaCategoria = categoriaService.insertUno(categoria);
        
	    if (nuevaCategoria != null) {
	        return new ResponseEntity<>("Categoria añadida correctamente", HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>("La categoria ya existe o hubo un error", HttpStatus.BAD_REQUEST);
	    }
		
		// POSTMAN: localhost:8445/categoria/add   
    }
	
	
	@Operation(
		    summary = "Modificar una categoria existente",
		    description = "Edita los datos de una categoria por su idCategoria. El idCategoria no se puede cambiar. Retorna mensaje, en funcion de valor entero de respuesta",
		    parameters = {
		    		@Parameter(
		    			    name = "idCategoria",
		    			    description = "idCategoria de la categoria que se desea modificar",
		    			    example = "1"
		    			)
		    },
		    requestBody = 	@io.swagger.v3.oas.annotations.parameters.RequestBody(
				    description = "Objeto Categoria con los datos actualizados (sin cambiar el idCategoria)",
				    required = true,
				    content = @Content(
				        schema = @Schema(implementation = Categoria.class),
				        examples = @ExampleObject(
				            name = "Ejemplo de actualización de una categoria",
				            value = """
							      {
    		       		              "nombre": "Finanzas",
		                    		  "descripcion": "Oportunidades en el área de contabilidad, auditoría y análisis financiero"
		                    	}
				            """
				        )
				    )
				),
		    responses = {
				    @ApiResponse(responseCode = "200", description = "OK. Categoría actualizada correctamente"),
				    @ApiResponse(responseCode = "404", description = "Not Found. Error: No se encontró la categoría"),
				    @ApiResponse(responseCode = "500", description = "Internal Server Error. Error desconocido") 		
		    }
		)
    // Actualizar una categoría existente (PUT)
    @PutMapping("/edit/{idCategoria}")
    public ResponseEntity<?> actualizarCategoria(@PathVariable int idCategoria, @RequestBody Categoria categoriaActualizado) {
    	Categoria categoriaExistente = categoriaService.buscarUno(idCategoria);
		if (categoriaExistente != null) {
			categoriaActualizado.setIdCategoria(idCategoria); // Asegurar el email no cambia.
			switch(categoriaService.updateUno(categoriaActualizado)) {
				case 1:  return new ResponseEntity<>("✅ Categoría actualizada correctamente", HttpStatus.OK);
				case 0:  return new ResponseEntity<>("❌ Error: No se encontró la categoría", HttpStatus.NOT_FOUND);
				default:  return new ResponseEntity<>("Error desconocido", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}	
		else
			return new ResponseEntity<String>("Esta categoria no existe", HttpStatus.NOT_FOUND);
		
		// POSTMAN: localhost:8445/categoria/edit/5
    }
    
	@Operation(
		    summary = "Eliminar una categoria",
		    description = "Elimina una categoria existente del sistema identificada por su idCategoria. Retorna mensaje, en funcion de valor entero de respuesta",
		    parameters = {
		    		@Parameter(
		    			    name = "idCategoria",
		    			    description = "idCategoria de la categoria que se desea modificar",
		    			    example = "1"
		    			)
		    },
		    responses = {
				    @ApiResponse(responseCode = "200", description = "OK. Categoría eliminada correctamente"),
				    @ApiResponse(responseCode = "404", description = "Not Found. No se encontró la categoría"),
				    @ApiResponse(responseCode = "500", description = "Internal Server Error. Error desconocido")		
		    }
		)
    // Eliminar una categoría (DELETE)
    @DeleteMapping("/delete/{idCategoria}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable int idCategoria) {
    	
		switch(categoriaService.deleteUno(idCategoria)) {
			case 1:  return new ResponseEntity<>("✅ Categoría eliminada correctamente", HttpStatus.OK);
			case 0:  return new ResponseEntity<>("❌ Error: No se encontró la categoría", HttpStatus.NOT_FOUND);
			default:  return new ResponseEntity<>("Error desconocido", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
		// POSTMAN: localhost:8445/categoria/delete/5   	
    }
       
}