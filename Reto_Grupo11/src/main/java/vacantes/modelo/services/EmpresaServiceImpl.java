package vacantes.modelo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vacantes.modelo.entities.Empresa;
import vacantes.modelo.entities.Usuario;
import vacantes.repository.EmpresaRepository;
import vacantes.repository.UsuarioRepository;

@Service
public class EmpresaServiceImpl implements EmpresaService {

	@Autowired
	private EmpresaRepository empresaRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public Empresa buscarUno(Integer id) {

		return empresaRepository.findById(id).orElse(null);
	}

	@Override
	public List<Empresa> buscarTodos() {

		return empresaRepository.findAll();
	}

	@Override
	public Empresa insertUno(Empresa empresa) {

		if (empresa.getIdEmpresa() != 0) {
			throw new IllegalArgumentException("El ID no debe ser proporcionado para crear una nueva empresa.");
		}

		Usuario usuario = empresa.getUsuario();

		if (usuario != null) {

			if (usuarioRepository.findByEmail(usuario.getEmail()) == null) {

				usuario = usuarioRepository.save(usuario);
			}
		} else {

			throw new IllegalArgumentException("El usuario debe ser proporcionado.");
		}

		empresa.setUsuario(usuario);

		return empresaRepository.save(empresa);
	}

	@Override
	public int updateUno(Empresa empresa) {
		if (empresaRepository.existsById(empresa.getIdEmpresa())) {

			empresaRepository.save(empresa);

			return 1;
		}
		return 0;
	}

	@Override
	public int deleteUno(Integer id) {

		if (empresaRepository.existsById(id)) {

			empresaRepository.deleteById(id);

			return 1;
		}
		return 0;
	}

	@Override
	public Empresa buscarPorNombre(String nombre) {
		// TODO Auto-generated method stub
		return empresaRepository.findByNombreEmpresa(nombre);
	}

	@Override
	public Empresa buscarPorUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return empresaRepository.findByUsuario(usuario).orElse(null);
	}

}
