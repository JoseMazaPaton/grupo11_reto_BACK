package vacantes.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vacantes.dto.LoginRequestDto;
import vacantes.dto.LoginResponseDto;
import vacantes.dto.RegistroRequestDto;
import vacantes.dto.RegistroResponseDto;
import vacantes.modelo.services.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

	 @Autowired
	    private AuthService authService;


	  //METODO CON RUTA PARA INICIAR SESION
	    @PostMapping("/login")
	    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginDto) {
	    	
	    	//Hacemos login con el metodo del service y guardamos las respuesta Dto que devuelve
	        LoginResponseDto response = authService.login(loginDto);
	        
	        return ResponseEntity.ok(response);
	    }

	    

	    //METODO CON RUTA PARA CERRAR SESIÓN
	    @PostMapping("/logout")
	    public ResponseEntity<Map<String, Object>> logout() {
	    	
	        // Limpiamos el contexto de seguridad (borra el usuario autenticado actual)
	        SecurityContextHolder.clearContext();

	        // Devolvemos un mensaje de exito
	        return ResponseEntity.ok(
	            Map.of("mensaje", "Sesión cerrada correctamente")
	        );
	    }
	    
	    
	    //METODO CON RUTA PARA REGISTRAR UN USUARIO
	    @PostMapping("/registro")
	    public ResponseEntity<RegistroResponseDto> registroUsuario(@RequestBody @Valid RegistroRequestDto registroDto) {
	    	
	    	
	    	//Damos de alta el usuario,los objetivos y guardamos la respuesta con el metodo del servicio
	    	//Todas las excepciones se controlan en el service tambien
	    	RegistroResponseDto respuesta = authService.altaUsuario(registroDto);

	    	
	    	return ResponseEntity.ok(respuesta);
	    	
	    }
	
}
