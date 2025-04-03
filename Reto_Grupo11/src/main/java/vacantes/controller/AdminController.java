package vacantes.controller;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vacantes.dto.AltaEmpresaRequestDto;
import vacantes.dto.AltaEmpresaResponseDto;
import vacantes.modelo.entities.Empresa;
import vacantes.modelo.entities.Usuario;
import vacantes.modelo.services.EmpresaServiceImpl;
import vacantes.modelo.services.UsuarioServiceImpl;
import vacantes.utils.PasswordGenerator;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {
	
	
	@Autowired
	private EmpresaServiceImpl eService;
	
	@Autowired
	private UsuarioServiceImpl uService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Transactional  // Si falla empresa, se revierte el usuario
	@PostMapping("/alta/empresa")
	public ResponseEntity<?> altaEmpresa(@RequestBody AltaEmpresaRequestDto altaEmpresaDto) {
		
		if (uService.buscarPorEmail(altaEmpresaDto.getEmail()) != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Ese email ya esta en uso, usa otro");
		}
		
		Usuario usuario = new Usuario();
		
		usuario.setEmail(altaEmpresaDto.getEmail());
		usuario.setNombre(altaEmpresaDto.getNombreUsuario());
		usuario.setApellidos(altaEmpresaDto.getApellidoUsuario());
		
		usuario.setEnabled(1);
		usuario.setFechaRegistro(LocalDate.now());
		usuario.setRol(usuario.getRol().EMPRESA);
		
		String password = PasswordGenerator.generateRandomPassword();
		
		usuario.setPassword(passwordEncoder.encode(password));
		
		uService.insertUno(usuario);
			
		Empresa empresa = new Empresa();
			
		empresa.setIdEmpresa(0);
		empresa.setCif(altaEmpresaDto.getCif());
		empresa.setNombreEmpresa(altaEmpresaDto.getNombreEmpresa());
		empresa.setDireccionFiscal(altaEmpresaDto.getDireccionFiscal());
		empresa.setPais(altaEmpresaDto.getPais());
		empresa.setUsuario(usuario);
		
		eService.insertUno(empresa);
		
		AltaEmpresaResponseDto response = new AltaEmpresaResponseDto();
		
		response.setEmail(altaEmpresaDto.getEmail());
		response.setPassword(password);
		response.setRol(usuario.getRol().EMPRESA.toString());
		
		return new ResponseEntity<AltaEmpresaResponseDto>(response, HttpStatus.CREATED);
		

		
	}
	
	

}
