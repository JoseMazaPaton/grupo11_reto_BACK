package vacantes.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta con los datos de una solicitud")
public class SolicitudResponseDto {

    @Schema(description = "Fecha de la solicitud", example = "2025-04-12")
    private Date fecha;

    @Schema(description = "Archivo subido por el candidato", example = "portafolio.pdf")
    private String archivos;

    @Schema(description = "Estado actual de la solicitud (0 = pendiente, 1 = adjudicada)", example = "0")
    private int estado;

    @Schema(description = "Nombre del archivo de curriculum", example = "cv_john_doe.pdf")
    private String curriculum;

    @Schema(description = "Nombre de la vacante solicitada", example = "Desarrollador Java")
    private String nombreVacante;

    @Schema(description = "Imagen asociada a la vacante", example = "vacante123.jpg")
    private String imagenVacante;

    @Schema(description = "Nombre de la empresa que ofrece la vacante", example = "Tech Solutions")
    private String nombreEmpresa;
}