package vacantes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para dar de alta un nuevo usuario administrador")
public class UsuarioRequestDto {

    @Schema(description = "Email del nuevo administrador", example = "admin@correo.com")
    private String email;

    @Schema(description = "Nombre del administrador", example = "Laura")
    private String nombre;

    @Schema(description = "Apellidos del administrador", example = "Pérez Gómez")
    private String apellidos;

    @Schema(description = "Contraseña del administrador", example = "Admin1234")
    private String password;
}
