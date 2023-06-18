package br.com.tcc.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "doutor")
public class Doutor extends Funcionario implements Serializable {

	private static final long serialVersionUID = 1415402698004881580L;

	@Column(name = "cro", length = 20, nullable = false, unique = true)
	private String cro;
	
}
