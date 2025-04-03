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
public class UsuarioResponseDto {

	private String email;
	
	private String nombre;
	
	private String apellidos;
	
	private Date fechaRegistro;
	
}
