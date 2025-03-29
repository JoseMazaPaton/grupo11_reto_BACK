package vacantes.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class DataUserConfiguration {
	
	/*
	@Bean
	public UserDetailsManager usersCustom(DataSource dataSource) {

		JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
		users.setUsersByUsernameQuery("select username,password,enabled from Usuarios u where username=?");
		users.setAuthoritiesByUsernameQuery("select u.username,p.nombre from Usuario_Perfiles up "
				+ "inner join usuarios u on u.username = up.username "
				+ "inner join perfiles p on p.id_perfil = up.id_perfil " + "where u.username = ?");

		return users;

	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable());
		// Los recursos estáticos no requieren autenticación
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("static/**").permitAll()
				
				// Las vistas públicas no requieren autenticación
				.requestMatchers("/signup", "/usuarios/registro", "/", "/home", "/login", "/logout", "/inicioSesion",
						"/public/**", "/listaProductos", "/eventos/destacados", "/eventos/cancelados",
						"/eventos/terminados", "/eventos/activos", "/eventos/detalleEvento/{id}")
				.permitAll()
				// .requestMatchers("/rest/encriptar/**").permitAll()
				// Todas las demás URLs de la Aplicación requieren autenticación
				// Asignar permisos a URLs por ROLES
				.requestMatchers("/eventos/editar/**").hasAnyAuthority("ROLE_ADMON")
	            .requestMatchers("/eventos/cancelar/**").hasAnyAuthority("ROLE_ADMON")
	            .requestMatchers("/eventos/eliminar/**").hasAnyAuthority("ROLE_ADMON")
				.requestMatchers("/app/producto/**").hasAnyAuthority("ROLE_ADMON").requestMatchers("/evento/editar/**")
				.hasAnyAuthority("ROLE_ADMON").requestMatchers("/usuarios/**").hasAnyAuthority("ROLE_ADMON")
				.requestMatchers("/app/perfiles/**", "reservas/misReservas").hasAnyAuthority("ROLE_CLIENTE").requestMatchers("/app/tipos/**")
				.hasAnyAuthority("ROLE_ADMON")
				// .requestMatchers("/eventos/tipos/**").hasAnyAuthority("ROLE_ADMON")
				.anyRequest().authenticated())
				// El formulario de Login no requiere autenticacion
				.formLogin(form -> form
						// .loginPage("/inicioSesion")
						.defaultSuccessUrl("/inicioSesion", true).permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/").permitAll());

		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	*/
	
	 	
	 	@Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .csrf(csrf -> csrf.disable()) // Desactiva CSRF para pruebas
	            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Permite todas las peticiones
	            .formLogin(login -> login.disable()) // Desactiva formulario de login
	            .httpBasic(basic -> basic.disable()); // Desactiva autenticación básica

	        return http.build();
	    }
	 	

}
