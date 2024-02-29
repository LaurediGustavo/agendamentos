package br.com.tcc.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "funcionario")
@Inheritance(strategy = InheritanceType.JOINED)
public class Funcionario implements Serializable {

	private static final long serialVersionUID = -8947289645334051925L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;
	
	@Column(name = "sobrenome", length = 100, nullable = false)
	private String sobrenome;
	
	@Column(name = "data_de_nascimento", nullable = false)
	private LocalDate dataDeNascimento;
	
	@Column(name = "cpf", length = 15, nullable = false, unique = true)
	private String cpf;
	
	@Column(name = "genero", length = 50, nullable = false)
	private String genero;
	
	@Column(name = "telefone", length = 15, nullable = false)
	private String telefone;

	@Column(name = "cep", length = 9)
	private String cep;

	@Column(name = "logradouro", length = 100, nullable = false)
	private String logradouro;
	
	@Column(name = "bairro", length = 100, nullable = false)
	private String bairro;
	
	@Column(name = "numero", nullable = false)
	private Integer numero;
	
	@Column(name = "bloco", length = 100, nullable = true)
	private String bloco;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo_funcionario_id")
	private TipoFuncionario tipoFuncionario;

	@Column(name = "desabilitado")
	private Boolean desabilitado;

	@Column(name = "email")
	private String email;

}
