package vacantes.modelo.services;

import java.util.List;

import vacantes.modelo.entities.Empresa;
import vacantes.modelo.entities.EstadoVacante;
import vacantes.modelo.entities.Vacante;

public interface VacanteService  extends CrudGenerico<Vacante, Integer> {
	
	Vacante buscarPorNombre (String nombre);
	List<Vacante> buscarPorEmpresa (Empresa empresa);
	List<Vacante> buscarPorEstado (EstadoVacante estatus);
}
