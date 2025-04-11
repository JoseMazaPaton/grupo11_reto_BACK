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
@Schema(description = "Respuesta al dar de alta una nueva empresa con su usuario")
public class AltaEmpresaResponseDto {

    @Schema(description = "Email generado para la empresa", example = "empresa@ejemplo.com")
    private String email;

    @Schema(description = "Contraseña generada automáticamente para el primer acceso", example = "T3mp0r4l#2025")
    private String password;

    @Schema(description = "Rol asignado al usuario", example = "EMPRESA")
    private String rol;
}
