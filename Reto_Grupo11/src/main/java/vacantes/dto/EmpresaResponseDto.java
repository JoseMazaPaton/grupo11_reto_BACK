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
@Schema(description = "DTO de respuesta con toda la información de una empresa registrada")
public class EmpresaResponseDto {
	
    @Schema(description = "ID de la empresa que publica la vacante", example = "1")
    private int idEmpresa;
	
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
