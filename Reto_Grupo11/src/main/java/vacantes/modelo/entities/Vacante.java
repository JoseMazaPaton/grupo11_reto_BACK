package vacantes.modelo.entities;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name="vacantes")
public class Vacante {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_vacante")
	private int idVacante;
	
	private String nombre;
	
	@Column(columnDefinition = "TEXT", nullable = false)
	private String descripcion;
	
	private Date fecha;
	private double salario;
	
	@Enumerated(EnumType.STRING)
	private EstadoVacante estatus;
	
	private boolean destacado;
	private String imagen;
	
	@Column(columnDefinition = "TEXT")
	private String detalles;
	
	@ManyToOne
	@JoinColumn(name="id_categoria")
	private Categoria categoria;
	
	@ManyToOne
	@JoinColumn(name="id_empresa")
	private Empresa empresa;
	
}
