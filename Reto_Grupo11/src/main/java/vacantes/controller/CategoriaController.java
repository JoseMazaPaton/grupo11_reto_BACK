package vacantes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // 🟢 Obtener todas las categorías (GET)
    @GetMapping("/listar")
    public List<Categoria> listarCategorias() {
        return categoriaService.buscarTodos();
    }

    // 🔵 Obtener una categoría por ID (GET)
    @GetMapping("/buscar/{id}")
    public Categoria buscarCategoria(@PathVariable int id) {
        return categoriaService.buscarUno(id);
    }

    // 🟠 Crear una nueva categoría (POST)
    @PostMapping("/crear")
    public Categoria crearCategoria(@RequestBody Categoria categoria) {
        return categoriaService.insertUno(categoria);
    }

    // 🟣 Actualizar una categoría existente (PUT)
    @PutMapping("/actualizar/{id}")
    public String actualizarCategoria(@PathVariable int id, @RequestBody Categoria categoria) {
        categoria.setIdCategoria(id);
        int resultado = categoriaService.updateUno(categoria);
        return (resultado == 1) ? "✅ Categoría actualizada correctamente" : "❌ Error: No se encontró la categoría";
    }

    // 🔴 Eliminar una categoría (DELETE)
    @DeleteMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable int id) {
        int resultado = categoriaService.deleteUno(id);
        return (resultado == 1) ? "✅ Categoría eliminada correctamente" : "❌ Error: No se encontró la categoría";
    }
}