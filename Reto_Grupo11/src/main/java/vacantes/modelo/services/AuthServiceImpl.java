package vacantes.modelo.services;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import vacantes.auth.JwtUtils;
import vacantes.dto.LoginRequestDto;
import vacantes.dto.LoginResponseDto;
import vacantes.dto.RegistroRequestDto;
import vacantes.dto.RegistroResponseDto;
import vacantes.dto.UsuarioResponseDto;
import vacantes.modelo.entities.Usuario;
import vacantes.repository.UsuarioRepository;

@Service
public class AuthServiceImpl implements AuthService{

	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
    private JwtUtils jwtUtils;
	
	@Autowired
	private UsuarioRepository uRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ModelMapper mapper;
	
	
	
	
	
	@Override
	public Usuario buscarUno(String email) {
		return uRepo.findById(email).orElse(null);
	}

	@Override
	public List<Usuario> buscarTodos() {
		return uRepo.findAll();
	}

	@Override
	public Usuario insertUno(Usuario usuario) {
		try {
			if (uRepo.existsById(usuario.getEmail())) {
				return null; // Me aseguro
				// Si lo encuentra devuelve nulo para no darlo de alta.
			}
			else 
				return uRepo.save(usuario);
			
		} catch (Exception e) {
			e.printStackTrace(); 
			return null;
		}
	}

	@Override
	public int updateUno(Usuario usuario) {
	    try {
	        if (uRepo.existsById(usuario.getEmail())) {
	            
	            uRepo.save(usuario);
	            return 1;
	        } else {
	            return 0; 
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1; 
	    }	
	}

	@Override
	public int deleteUno(String email) {
	    try {
			if (uRepo.existsById(email)) {
				uRepo.deleteById(email); 
				return 1; // Si lo encuenta, y lo borra, devuelvo 1.
			}
			else 
				return 0; // Si no existe, devuelvo 0
				
		} catch (Exception e) {
			e.printStackTrace(); // para las pruebas hago un syso de todo lo ocurrido.
			return -1; // Si se casca, devuelvo -1
		}
	}

	//METODO PARA LOGIN DE UN USUARIO
		@Override
		public LoginResponseDto login(LoginRequestDto loginDto) {
			try {
				System.out.println("comienzo");
				System.out.println(loginDto.getEmail());
				System.out.println(loginDto.getPassword());
				// Autenticamos el usuario con la info del Dto que hemos creado con el AuthenticationManager
			    Authentication authentication = authenticationManager.authenticate(
			        new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
			    System.out.println("aqui ya no sigo, depues auth");
			    System.out.println(authentication);
			    SecurityContextHolder.getContext().setAuthentication(authentication);
			    System.out.println("Voy a buscar al usuario");
			    // Recuperamos el usuario desde la base de datos
			    Usuario usuario = buscarUno(loginDto.getEmail());
			    System.out.println(usuario);
			    
			    // Generamos el token JWT con JwtUtils 
			    String token = jwtUtils.generateToken(usuario);
			    
		        // Mapeamos el usuario al dto de respuesta que hemos creado.( para evitar que salgan las relaciones)
		        //y devolvemos los datos del usuario + el token 
			    LoginResponseDto response = mapper.map(usuario, LoginResponseDto.class);
			    response.setToken(token);
			    
			    return response;

			} catch (Exception e) {
			    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado en el login: " + e.getMessage());
			}
		}

		@Override
		@Transactional // Esto lo ponemos por si hay algun fallo que se revierta todo (por ejemplo se guarda usuario pero el resto falla)
		public RegistroResponseDto altaUsuario(RegistroRequestDto registroDto) {
		    try {
		        if (uRepo.existsById(registroDto.getUsuario().getEmail())) {
		            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está dado de alta.");
		        }

		        // Guardamos la contraseña para encriptarla
		        // También sirve en sí para más adelante si quisiera implementar la lógica de enviarla por email
		        String contraseña = registroDto.getUsuario().getPassword();

		        // Mapeamos usuario y objetivo a sus entidades
		        Usuario usuario = mapper.map(registroDto.getUsuario(), Usuario.class);


		        // Agregamos a cada entidad las relaciones (bidireccional)
		        // Encriptamos la contraseña y la añadimos
		        // Le añadimos la fechaDeRegistro
		        // El rol no hace falta porque lo hemos puesto por defecto como = ROL_USUARIO en la entidad
		        usuario.setPassword(passwordEncoder.encode(contraseña));
		        usuario.setFechaRegistro(LocalDate.now());
		        //descomentar para crear usuario con rol admon
		        //usuario.setRol(usuario.getRol().ADMON);

		        // Guardamos los datos del usuario (se guarda también objetivo por cascada)
		        uRepo.save(usuario);



		        // Después volvemos a mapearlo en forma de respuestaDto
		        UsuarioResponseDto usuarioDto = mapper.map(usuario, UsuarioResponseDto.class);
		        
		        // Generamos el token y se lo añadimos a la respuesta
		        // ESTO ES OPCIONAL-- es por si queremos iniciar sesión nada más registrarnos
		        String token = jwtUtils.generateToken(usuario);

		        // Devolvemos un Dto para la respuesta del controller
		        return new RegistroResponseDto(usuarioDto, token);

		    } catch (IllegalArgumentException e) {
		        throw e;
		    } catch (Exception e) {
		        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado al registrar el usuario: " + e.getMessage());
		    }
	}

}
