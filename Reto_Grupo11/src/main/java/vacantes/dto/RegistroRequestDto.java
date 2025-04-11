package vacantes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para registrar un nuevo usuario en el sistema")
public class RegistroRequestDto {

    @Schema(description = "Datos del usuario que se desea registrar", 
            example = "{ \"email\": \"usuario@correo.com\", \"nombre\": \"Juan\", \"apellidos\": \"Pérez\", \"password\": \"contraseña123\" }")
    @Valid
    private UsuarioRequestDto usuario;
}
