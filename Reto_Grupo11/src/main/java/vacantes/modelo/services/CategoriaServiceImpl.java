package vacantes.modelo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vacantes.modelo.entities.Categoria;
import vacantes.repository.CategoriaRepository;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public Categoria buscarUno(Integer id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Categoria> buscarTodos() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria insertUno(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public int updateUno(Categoria categoria) {
        if (categoriaRepository.existsById(categoria.getIdCategoria())) {
            categoriaRepository.save(categoria);
            return 1; // 1 indica éxito
        }
        return 0; // 0 indica que no se actualizó
    }

    @Override
    public int deleteUno(Integer id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return 1; // Eliminación exitosa
        }
        return 0; // No se encontró la categoría
    }

	@Override
	public Categoria buscarPorNombre(String nombre) {
		// TODO Auto-generated method stub
		return categoriaRepository.findByNombre(nombre);
	}
}