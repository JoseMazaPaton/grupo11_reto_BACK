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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import vacantes.dto.LoginRequestDto;
import vacantes.dto.LoginResponseDto;
import vacantes.dto.RegistroRequestDto;
import vacantes.dto.RegistroResponseDto;
import vacantes.modelo.services.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "Gestiones relacionadas con la autenticacion, tales como logearte y registrarte")
public class AuthController {

	 @Autowired
	    private AuthService authService;


	  //METODO CON RUTA PARA INICIAR SESION
		 @Operation(
			        summary = "Iniciar sesión",
			        description = "Autentica a un usuario mediante su email y contraseña. Devuelve un token JWT si es válido."
			    )
			    @ApiResponses(value = {
			        @ApiResponse(
			            responseCode = "200",
			            description = "Login correcto",
			            content = @Content(
			                mediaType = "application/json",
			                schema = @Schema(implementation = LoginResponseDto.class),
			                examples = @ExampleObject(
			                    name = "Respuesta ejemplo",
			                    value = "{ \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6...\", \"rol\": \"USUARIO\" }"
			                )
			            )
			        ),
			        @ApiResponse(responseCode = "500", description = "Error inesperado en el login")
			    })
	    @PostMapping("/login")
	    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginDto) {
	    	
	    	//Hacemos login con el metodo del service y guardamos las respuesta Dto que devuelve
	        LoginResponseDto response = authService.login(loginDto);
	        
	        return ResponseEntity.ok(response);
	    }

	    

	    //METODO CON RUTA PARA CERRAR SESIÓN - - - - - NO SE USARA
	    // @PostMapping("/logout")
	    // public ResponseEntity<Map<String, Object>> logout() {
	    	
	    //      // Limpiamos el contexto de seguridad (borra el usuario autenticado actual)
	    //      SecurityContextHolder.clearContext();
	    //
	    //      Devolvemos un mensaje de exito
	    //      return ResponseEntity.ok(
	    //         Map.of("mensaje", "Sesión cerrada correctamente")
	    //     );
	    //  }
	    
	    
	    //METODO CON RUTA PARA REGISTRAR UN USUARIO
		 @Operation(
			        summary = "Registrar usuario",
			        description = "Registra un nuevo usuario en el sistema con sus datos personales y credenciales."
			    )
			    @ApiResponses(value = {
			        @ApiResponse(
			            responseCode = "200",
			            description = "Registro correcto",
			            content = @Content(
			                mediaType = "application/json",
			                schema = @Schema(implementation = RegistroResponseDto.class),
			                examples = @ExampleObject(
			                    name = "Respuesta exitosa",
			                    value = "{\n" +
			                            "  \"usuario\": {\n" +
			                            "    \"email\": \"usuario@correo.com\",\n" +
			                            "    \"nombre\": \"Juan\",\n" +
			                            "    \"apellidos\": \"Pérez\",\n" +
			                            "    \"fechaRegistro\": \"2025-04-10T12:00:00\"\n" +
			                            "  },\n" +
			                            "  \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNjE3NjY5MzI2LCJleHBpcmVkVG9rZW4iOiJldG9rZW4= \"\n" +
			                            "}"
			                )
			            )
			        ),
			        @ApiResponse(
			            responseCode = "409",
			            description = "Error de validación o email duplicado",
			            content = @Content(
			                mediaType = "application/json",
			                schema = @Schema(type = "object", example = "{\n" +
			                            "  \"mensaje\": \"El email proporcionado ya está registrado\"\n" +
			                            "}")
			            )
			        )
			    })
	    @PostMapping("/registro")
	    public ResponseEntity<RegistroResponseDto> registroUsuario(@RequestBody @Valid RegistroRequestDto registroDto) {
	    	
	    	
	    	//Damos de alta el usuario,los objetivos y guardamos la respuesta con el metodo del servicio
	    	//Todas las excepciones se controlan en el service tambien
	    	RegistroResponseDto respuesta = authService.altaUsuario(registroDto);

	    	
	    	return ResponseEntity.ok(respuesta);
	    	
	    }
	
}
