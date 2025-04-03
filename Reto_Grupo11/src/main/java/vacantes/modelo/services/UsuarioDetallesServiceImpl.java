package vacantes.modelo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vacantes.modelo.entities.Rol;
import vacantes.modelo.entities.Usuario;
import vacantes.repository.UsuarioRepository;

@Service
public class UsuarioDetallesServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository uRepo;

    /**
     * Método que se ejecuta automáticamente cuando Spring intenta autenticar a un usuario.
     * 
     * @param email El email del usuario que se quiere autenticar.
     * @return UserDetails con la info del usuario si lo encuentra y está habilitado.
     * @throws UsernameNotFoundException si el usuario no existe o está suspendido.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = uRepo.findByEmail(email);
                //.orElseThrow(() -> new UsernameNotFoundException("No se encontró un usuario con el email: " + email));

        if (usuario.getEnabled() == 0) {
            throw new UsernameNotFoundException("El usuario está suspendido: " + email);
        }

        // Obtenemos el nombre del rol (por ejemplo "ROLE_ADMON")
        String nombreRol = usuario.getRol().toString();

        // Creamos la autoridad para Spring Security usando ese rol
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(nombreRol);

        return usuario;
    }
}
