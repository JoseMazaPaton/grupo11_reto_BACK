package vacantes.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vacantes.modelo.entities.EstadoVacante;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con toda la información de una vacante publicada")
public class VacanteResponseDto {

    @Schema(description = "Identificador único de la vacante", example = "7")
    private int idVacante;

    @Schema(description = "Nombre del puesto o vacante", example = "Desarrollador Frontend")
    private String nombre;

    @Schema(description = "Descripción del puesto", example = "Desarrollo de interfaces con Angular, consumo de APIs REST, y diseño responsive.")
    private String descripcion;

    @Schema(description = "Fecha de publicación de la vacante", example = "2025-04-12")
    private Date fecha;

    @Schema(description = "Salario bruto mensual en euros", example = "2500.00")
    private double salario;

    @Schema(description = "Estado actual de la vacante", example = "CREADA")
    private EstadoVacante estatus;

    @Schema(description = "Indica si la vacante es destacada", example = "true")
    private boolean destacado;

    @Schema(description = "Nombre del archivo de imagen asociado", example = "frontend-vacante.jpg")
    private String imagen;

    @Schema(description = "Detalles adicionales del puesto", example = "Ofrecemos beneficios sociales y formación continua.")
    private String detalles;

    @Schema(description = "Categoría profesional de la vacante", example = "Desarrollo Web")
    private String categoria;

    @Schema(description = "Nombre de la empresa que ofrece la vacante", example = "Tech Solutions")
    private String empresa;
}
