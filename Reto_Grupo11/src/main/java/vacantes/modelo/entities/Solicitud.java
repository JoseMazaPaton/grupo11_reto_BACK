package vacantes.modelo.entities;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="solicitudes")
public class Solicitud {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_solicitud")
	private int idSolicitud;
	
	private Date fecha;
	private String archivo;
	private String comentarios;
	
	@Column(columnDefinition = "TINYINT(0)")
	private int estado;
	
	private String curriculum;
	
	@ManyToOne
	@JoinColumn(name="id_vacante")
	private Vacante vacante;
	
	@ManyToOne
	@JoinColumn(name="email")
	private Usuario usuario;
}
