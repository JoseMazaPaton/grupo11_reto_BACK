package vacantes.modelo.services;

import vacantes.dto.LoginRequestDto;
import vacantes.dto.LoginResponseDto;
import vacantes.dto.RegistroRequestDto;
import vacantes.dto.RegistroResponseDto;
import vacantes.modelo.entities.Usuario;

public interface AuthService extends CrudGenerico<Usuario, String>{
	
	LoginResponseDto login(LoginRequestDto loginDto);
	RegistroResponseDto altaUsuario (RegistroRequestDto registroDto);

}