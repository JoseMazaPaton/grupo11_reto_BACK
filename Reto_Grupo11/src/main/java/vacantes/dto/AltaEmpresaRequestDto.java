package vacantes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AltaEmpresaRequestDto {

	
	private String cif;
	private String nombreEmpresa;
	private String direccionFiscal;
	private String pais;
	private String email;
	private String nombreUsuario;
	private String apellidoUsuario;
}
