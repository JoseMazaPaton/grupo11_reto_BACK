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
@Schema(description = "Datos necesarios para crear o actualizar una solicitud")
public class SolicitudRequestDto {

    @Schema(description = "Nombre del archivo enviado", example = "portafolio.pdf")
    private String archivo;

    @Schema(description = "Comentarios adicionales del candidato", example = "Estoy especialmente interesado en esta vacante por mi experiencia previa.")
    private String comentarios;

    @Schema(description = "Nombre del archivo de CV adjunto", example = "cv_john_doe.pdf")
    private String curriculum;

    @Schema(description = "Nombre de la vacante a la que se postula", example = "Desarrollador Java")
    private String nombreVacante;
}
