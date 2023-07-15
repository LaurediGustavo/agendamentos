package br.com.tcc.entity;

import java.io.Serializable;

import br.com.tcc.enumerador.RoleNameEnum;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "role")
public class Role implements Serializable, GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "role_name", nullable = false, unique = true)
	private RoleNameEnum roleName;

	@Override
	public String getAuthority() {
		return this.roleName.toString();
	}

}
