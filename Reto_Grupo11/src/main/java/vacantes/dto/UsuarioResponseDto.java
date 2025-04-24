package vacantes.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vacantes.modelo.entities.Rol;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta con los datos públicos de un usuario")
public class UsuarioResponseDto {

    @Schema(description = "Email del usuario", example = "usuario@correo.com")
    private String email;

    @Schema(description = "Nombre del usuario", example = "Ana")
    private String nombre;

    @Schema(description = "Apellidos del usuario", example = "López Ramírez")
    private String apellidos;

    @Schema(description = "Fecha en la que se registró el usuario", example = "2024-05-12")
    private Date fechaRegistro;
    
    private String rol;
    private Integer enabled;
}