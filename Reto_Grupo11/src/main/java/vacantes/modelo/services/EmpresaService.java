package vacantes.modelo.services;

import vacantes.modelo.entities.Empresa;
import vacantes.modelo.entities.Usuario;


public interface EmpresaService extends CrudGenerico<Empresa, Integer> {

		Empresa buscarPorNombre (String nombre);
		Empresa buscarPorUsuario (Usuario usuario);
		Empresa buscarPorEmail (String email);
	} 
