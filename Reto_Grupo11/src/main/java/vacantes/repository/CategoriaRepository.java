package vacantes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes.modelo.entities.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

	public Categoria findByNombre(String nombre);
}
