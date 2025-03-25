package vacantes.modelo.entities;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="usuarios")
public class Usuario {
	
	@Id
	private String email;
	
	private String nombre;
	private String apellidos;
	private String password;
	private int enabled;
	
	@Column(name="fecha_Registro")
	private Date fechaRegistro;
	
	@Enumerated(EnumType.STRING)
	private Rol rol;
}
