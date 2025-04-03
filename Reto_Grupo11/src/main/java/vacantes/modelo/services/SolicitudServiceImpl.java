package vacantes.modelo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vacantes.modelo.entities.Solicitud;
import vacantes.repository.SolicitudRepository;

@Service
public class SolicitudServiceImpl implements SolicitudService {

	@Autowired
	private SolicitudRepository sRepo;
	
	@Override
	public Solicitud buscarUno(Integer clavePk) {
		// TODO Auto-generated method stub
		return sRepo.findById(clavePk).orElse(null);
	}

	@Override
	public List<Solicitud> buscarTodos() {
		// TODO Auto-generated method stub
		return sRepo.findAll();
	}

	@Override
	public Solicitud insertUno(Solicitud entidad) {
		try {
				if (sRepo.existsById(entidad.getIdSolicitud()))
					return null;
				else
					return sRepo.save(entidad);
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	@Override
	public int updateUno(Solicitud entidad) {
		try {
			if (sRepo.existsById(entidad.getIdSolicitud())) {
				sRepo.save(entidad);
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
			if (sRepo.existsById(clavePk)) {
				sRepo.deleteById(clavePk);
				return 1;
			}
			else 
				return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

}
