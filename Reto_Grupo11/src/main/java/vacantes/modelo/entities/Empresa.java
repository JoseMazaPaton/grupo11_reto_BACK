package vacantes.modelo.entities;

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
@Table(name="empresas")
public class Empresa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_empresa")
	private int idEmpresa;
	
	private String cif;
	
	@Column(name="nombre_empresa")
	private String nombreEmpresa;
	
	@Column(name="direccion_fiscal")
	private String direccionFiscal;
	
	private String pais;
	
	@ManyToOne
	@JoinColumn(name="email")
	private Usuario usuario;

}
