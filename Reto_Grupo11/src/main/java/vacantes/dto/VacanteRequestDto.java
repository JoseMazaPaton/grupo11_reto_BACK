package vacantes.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacanteRequestDto {
	
	private String nombre;
	private String descripcion;
	private double salario;
	private String imagen;
	private String detalles;
	private String nombreCategoria;
	private String nombreEmpresa;

}
