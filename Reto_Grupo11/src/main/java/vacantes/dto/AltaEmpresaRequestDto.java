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
@Schema(description = "Datos necesarios para dar de alta una empresa")
public class AltaEmpresaRequestDto {

    @Schema(description = "Email del nuevo usuario empresa", example = "empresa@correo.com")
    private String email;

    @Schema(description = "Nombre del usuario", example = "Carlos")
    private String nombreUsuario;

    @Schema(description = "Apellidos del usuario", example = "García Pérez")
    private String apellidoUsuario;

    @Schema(description = "Nombre de la empresa", example = "Tech Consulting SL")
    private String nombreEmpresa;

    @Schema(description = "CIF de la empresa", example = "B12345678")
    private String cif;

    @Schema(description = "Dirección fiscal de la empresa", example = "Calle Mayor 45, Madrid")
    private String direccionFiscal;

    @Schema(description = "País de la empresa", example = "España")
    private String pais;
}