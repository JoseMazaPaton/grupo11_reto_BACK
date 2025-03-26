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
import vacantes.modelo.entities.Categoria;
import vacantes.modelo.services.CategoriaService;


@RestController
@RequestMapping("/categoria")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    
	/***** CRUD *****/
    
    // 🟢 Obtener todas las categorías (GET)
    @GetMapping("/all")
    public ResponseEntity<List<Categoria>> listarCategorias() {
		return new ResponseEntity<List<Categoria>>(categoriaService.buscarTodos(), HttpStatus.OK);
		
		// POSTMAN: localhost:8445/categoria/all
    }

    // 🔵 Obtener una categoría por ID (GET)
    @GetMapping("/detail/{idCategoria}")
    public ResponseEntity<?> buscarCategoria(@PathVariable int idCategoria) {
        Categoria categoria = categoriaService.buscarUno(idCategoria);
		if (categoria != null)
			return new ResponseEntity<Categoria>(categoria, HttpStatus.OK);
		else
			return new ResponseEntity<String>("Este categoria no existe", HttpStatus.NOT_FOUND);
		
		// POSTMAN: localhost:8445/categoria/detail/1  
    }
   
    // 🟠 Crear una nueva categoría (POST)
    @PostMapping("/add")
    public ResponseEntity<?> crearCategoria(@RequestBody Categoria categoria) {
        Categoria nuevaCategoria = categoriaService.insertUno(categoria);
        
	    if (nuevaCategoria != null) {
	        return new ResponseEntity<>("Categoria añadida correctamente", HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>("El categoria ya existe o hubo un error", HttpStatus.BAD_REQUEST);
	    }
		
		// POSTMAN: localhost:8445/categoria/add   
    }
    
    // 🟣 Actualizar una categoría existente (PUT)
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
    
    // 🔴 Eliminar una categoría (DELETE)
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