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
@Table(name = "agendamento_chatbot")
public class AgendamentoChatBot implements Serializable {

	private static final long serialVersionUID = 8249491343728395751L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "procedimento_id")
	private Procedimento procedimento;

	@Column(name = "horario")
	private LocalDateTime horario;

	@Column(name = "chat_id")
	private Long chatId;

	@Column(name = "cpf", length = 14)
	private String cpf;

}
