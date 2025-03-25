package vacantes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes.modelo.entities.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {

}
