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
public class SolicitudResponseDto {
	
	private Date fecha;
	private String archivos;
	private int estado;
	private String curriculum;
	private String nombreVacante;
	private String imagenVacante;
	private String nombreEmpresa;
}
