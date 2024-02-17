package br.com.tcc.entity;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "procedimento")
public class Procedimento implements Serializable {

	@Serial
	private static final long serialVersionUID = 5288062917051456982L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "tratamento", length = 100, nullable = false)
	private String tratamento;
	
	@Column(name = "tempo", length = 100, nullable = false)
	private String tempo;
	
	@Column(name = "valor", precision = 18, scale = 2, nullable = false)
	private BigDecimal valor;

	@Column(name = "desabilitado")
	private Boolean desabilitado;
	
}
