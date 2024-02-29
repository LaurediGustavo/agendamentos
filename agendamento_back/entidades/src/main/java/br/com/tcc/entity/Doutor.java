package br.com.tcc.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter@Getter
@Entity
@Table(name = "doutor")
public class Doutor extends Funcionario implements Serializable {

	private static final long serialVersionUID = 1415402698004881580L;

	@Column(name = "cro", length = 20, nullable = false, unique = true)
	private String cro;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "doutor_procedimento",
			joinColumns = @JoinColumn(name = "doutor_id"),
			inverseJoinColumns = @JoinColumn(name = "procedimento_id")
	)
	private List<Procedimento> procedimentos;

	public Doutor(Long id, String nome, String sobrenome, LocalDate dataDeNascimento, String cpf, String genero, String telefone, String cep, String logradouro, String bairro, Integer numero, String bloco, TipoFuncionario tipoFuncionario, String cro, Boolean desabilitado, String email) {
		super(id, nome, sobrenome, dataDeNascimento, cpf, genero, telefone, cep, logradouro, bairro, numero, bloco, tipoFuncionario, desabilitado, email);
		this.cro = cro;
	}

	public Doutor() {
		super();
	}

}
