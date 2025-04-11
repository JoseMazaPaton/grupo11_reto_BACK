package vacantes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta al registrar un nuevo usuario, incluye el token de autenticación")
public class RegistroResponseDto {

    @Schema(description = "Datos del usuario registrado", 
            example = "{ \"email\": \"usuario@correo.com\", \"nombre\": \"Juan\", \"apellidos\": \"Pérez\", \"fechaRegistro\": \"2025-04-10T12:00:00\" }")
    private UsuarioResponseDto usuario;

    @Schema(description = "Token JWT generado para autenticar al usuario", 
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNjE3NjY5MzI2LCJleHBpcmVkVG9rZW4iOiJldG9rZW4= ")
    private String token;
}
