package vacantes.modelo.services;

import vacantes.modelo.entities.Empresa;


public interface EmpresaService extends CrudGenerico<Empresa, Integer> {

		Empresa buscarPorNombre (String nombre);
	} 
