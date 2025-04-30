package vacantes.modelo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vacantes.modelo.entities.Empresa;
import vacantes.modelo.entities.EstadoVacante;
import vacantes.modelo.entities.Vacante;
import vacantes.repository.VacanteRepository;

@Service
public class VacanteServiceImpl implements VacanteService {
	
	@Autowired
	private VacanteRepository vRepo;
	
	@Override
	public Vacante buscarUno(Integer clavePk) {
		return vRepo.findById(clavePk).orElse(null);
	}

	@Override
	public List<Vacante> buscarTodos() {
		// TODO Auto-generated method stub
		return vRepo.findAll();
	}

	@Override
	public Vacante insertUno(Vacante entidad) {
		
		try {
			if (vRepo.existsById(entidad.getIdVacante()))
				return null;
			else
				return vRepo.save(entidad);
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
	}

	@Override
	public int updateUno(Vacante entidad) {
		
		try {
			if (vRepo.existsById(entidad.getIdVacante())) {
				vRepo.save(entidad);
				return 1;
			}
				
			else
				return 0;
			
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int deleteUno(Integer clavePk) {
		
		try {
			if (vRepo.existsById(clavePk)) {
				vRepo.deleteById(clavePk);
				return 1;
			}
			else 
				return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public Vacante buscarPorNombre(String nombre) {
		
		return vRepo.findByNombre(nombre);
	}

	@Override
	public List<Vacante> buscarPorEmpresa(Empresa empresa) {
		// TODO Auto-generated method stub
		return vRepo.findByEmpresa(empresa);
	}
	
	@Override
	public List<Vacante> buscarPorEstado(EstadoVacante estatus) {
		return vRepo.findByEstatus(estatus);
	}

}
