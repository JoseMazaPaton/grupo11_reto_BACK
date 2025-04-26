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
@Schema(description = "DTO de respuesta con la información básica de una vacante")
public class VacanteResponseDto {

    @Schema(description = "ID de la vacante", example = "7")
    private int idVacante;

    @Schema(description = "Nombre de la vacante", example = "Desarrollador Frontend")
    private String nombre;

    @Schema(description = "Descripción de la vacante", example = "Trabajo en Angular y APIs REST")
    private String descripcion;

    @Schema(description = "Fecha de publicación", example = "2025-04-12")
    private Date fecha;

    @Schema(description = "Salario ofertado", example = "2500.00")
    private double salario;

    @Schema(description = "Estado de la vacante", example = "CREADA")
    private String estatus;  // 👈 CAMBIAMOS a String

    @Schema(description = "Indica si la vacante es destacada", example = "true")
    private boolean destacado;

    @Schema(description = "Nombre del archivo de imagen asociado", example = "frontend.jpg")
    private String imagen;

    @Schema(description = "Detalles adicionales", example = "Contrato indefinido, jornada completa")
    private String detalles;

    @Schema(description = "Nombre de la categoría asociada", example = "Desarrollo Web")
    private String categoria;

    @Schema(description = "Nombre de la empresa", example = "Tech Solutions")
    private String empresa;
}