package vacantes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroResponseDto {

	//DATOS DEL USUARIO
    private UsuarioResponseDto usuario;
       
    //TOKEN
    private String token;
}
