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
@Schema(description = "DTO que representa los datos necesarios para crear o modificar una vacante")
public class VacanteRequestDto {

    @Schema(description = "Título de la vacante", example = "Desarrollador Frontend")
    private String nombre;

    @Schema(description = "Descripción detallada del puesto", example = "Buscamos un perfil con experiencia en Angular y buenas prácticas de UX.")
    private String descripcion;

    @Schema(description = "Salario bruto mensual en euros", example = "2500.00")
    private double salario;

    @Schema(description = "Nombre del archivo de imagen relacionado con la vacante", example = "frontend-vacante.jpg")
    private String imagen;

    @Schema(description = "Detalles adicionales o condiciones del puesto", example = "Contrato indefinido. Teletrabajo parcial.")
    private String detalles;

    @Schema(description = "Nombre de la categoría asociada a la vacante", example = "Desarrollo Web")
    private String nombreCategoria;

    @Schema(description = "Nombre de la empresa que publica la vacante", example = "Tech Solutions")
    private String nombreEmpresa;
}
