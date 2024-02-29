package br.com.tcc.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Entity
@Table(name = "usuario")
public class Ususario implements Serializable, UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_name", nullable = false, length = 100, unique = true)
	private String userName;

	@Column(name = "password", nullable = false, length = 100)
	private String password;

	@ManyToMany
	@JoinTable(name = "user_role",
			   joinColumns = @JoinColumn(name = "usuario_id"),
			   inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "funcionario_id", nullable = false)
	private Funcionario funcionario;

	@Transient
	private String passwordSemCriptografia;

	@Transient
	private String email;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
