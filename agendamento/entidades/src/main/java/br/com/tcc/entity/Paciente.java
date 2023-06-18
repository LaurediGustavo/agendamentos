package br.com.tcc.entity;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "paciente")
public class Paciente implements Serializable {

	private static final long serialVersionUID = -3304973319884070682L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;
	
	@Column(name = "sobrenome", length = 100, nullable = false)
	private String sobrenome;
	
	@Column(name = "idade", nullable = true)
	private Integer idade;
	
	@Column(name = "cpf", length = 14, nullable = false)
	private String cpf;
	
	@Column(name = "genero", length = 50, nullable = true)
	private String genero;
	
	@Column(name = "telefone", length = 15, nullable = true)
	private String telefone;
	
	@Column(name = "informacoesadicionais", length = 1000, nullable = true)
	private String informacoesAdicionais;
	
	@Column(name = "logradouro", length = 100, nullable = true)
	private String logradouro;
	
	@Column(name = "bairro", length = 100, nullable = true)
	private String bairro;
	
	@Column(name = "numero", nullable = true)
	private Integer numero;
	
	@Column(name = "bloco", length = 100, nullable = true)
	private String bloco;
	
	@Column(name = "parentesco", length = 50, nullable = true)
	private String parentesco;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "responsavel_paciente_id", nullable = true)
	private Paciente responsavelPacienteId;
	
	@Column(name = "chatid", length = 50, nullable = true)
	private Long chatId;
	
}
