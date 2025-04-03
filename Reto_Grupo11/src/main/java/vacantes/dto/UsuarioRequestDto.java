package vacantes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDto {
	
	private String email;
	
	private String nombre;
	
	private String apellidos;
	
	private String password;
	
}
