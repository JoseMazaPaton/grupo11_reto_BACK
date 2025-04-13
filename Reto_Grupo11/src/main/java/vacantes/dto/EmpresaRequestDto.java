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
@Schema(description = "DTO que representa los datos necesarios para crear o modificar una empresa")
public class EmpresaRequestDto {

    @Schema(description = "CIF de la empresa que publica la vacante", example = "A12345678")
    private String cif;

    @Schema(description = "Nombre de la empresa que publica la vacante", example = "Tech Solutions")
    private String nombreEmpresa;

    @Schema(description = "Direccion Fiscal de la empresa que publica la vacante", example = "Calle Tecnología, 123")
    private String direccionFiscal;

    @Schema(description = "Pais de la empresa que publica la vacante", example = "España")
    private String pais;

    @Schema(description = "Email del usuario de la empresa que publica la vacante", example = "empresa1@correo.com")
    private String email;


}
