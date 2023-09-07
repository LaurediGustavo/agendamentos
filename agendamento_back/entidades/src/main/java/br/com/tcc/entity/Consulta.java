package br.com.tcc.entity;

import br.com.tcc.enumerador.StatusConsultaEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

	@ManyToMany
	@JoinTable(
			name = "consulta_procedimento",
			joinColumns = @JoinColumn(name = "consulta_id"),
			inverseJoinColumns = @JoinColumn(name = "procedimento_id")
	)
	private List<Procedimento> procedimentos;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private StatusConsultaEnum status;

	@Column(name = "valortotal", nullable = false)
	private BigDecimal valorTotal;

	@Column(name = "tempoaproximado", nullable = false)
	private LocalTime tempoAproximado;

}
