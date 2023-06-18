package br.com.tcc.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UsuarioDto {

	private String userName;

	private String password;

	private List<RoleDto> roles;
	
}
