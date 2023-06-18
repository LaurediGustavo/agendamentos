package br.com.tcc.entity;

import br.com.tcc.enumerador.StatusConsultaEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "consulta")
public class Consulta implements Serializable {

	private static final long serialVersionUID = 8249491343728395751L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "datahorainicio", nullable = false)
	private LocalDateTime dataHoraInicio;
	
	@Column(name = "datahorafinal", nullable = false)
	private LocalDateTime dataHoraFinal;
	
	@ManyToOne
	@JoinColumn(name = "paciente_id", nullable = false)
	private Paciente paciente;

	@ManyToOne
	@JoinColumn(name = "doutor_id", nullable = false)
	private Doutor doutor;
	
	@ManyToOne
	@JoinColumn(name = "procedimento_id", nullable = false)
	private Procedimento procedimento;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private StatusConsultaEnum status;

}
