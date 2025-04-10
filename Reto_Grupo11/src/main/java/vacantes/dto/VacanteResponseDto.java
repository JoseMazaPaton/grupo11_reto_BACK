package vacantes.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vacantes.modelo.entities.EstadoVacante;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacanteResponseDto {
	 
	private int idVacante;
	private String nombre;
	private String descripcion;
	private Date fecha;
	private double salario;
	private EstadoVacante estatus;
	private boolean destacado;
	private String imagen;
	private String detalles;
	private String categoria;
	private String empresa;
	
}
