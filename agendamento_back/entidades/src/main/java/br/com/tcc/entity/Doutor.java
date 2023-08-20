package br.com.tcc.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter@Getter
@Entity
@Table(name = "doutor")
public class Doutor extends Funcionario implements Serializable {

	private static final long serialVersionUID = 1415402698004881580L;

	@Column(name = "cro", length = 20, nullable = false, unique = true)
	private String cro;

	public Doutor(Long id, String nome, String sobrenome, Integer idade, String cpf, String genero, String telefone, String logradouro, String bairro, Integer numero, String bloco, TipoFuncionario tipoFuncionario, String cro) {
		super(id, nome, sobrenome, idade, cpf, genero, telefone, logradouro, bairro, numero, bloco, tipoFuncionario);
		this.cro = cro;
	}

	public Doutor() {
		super();
	}

}
