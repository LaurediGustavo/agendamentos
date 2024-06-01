package br.com.tcc.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsistirException extends Exception {

	private static final long serialVersionUID = -7938983426568873552L;

	private Map<String, Object> erros;

	private List<String> erroSimples;
	
	public ConsistirException() {
		this.erros = new HashMap<>();
		this.erroSimples = new ArrayList<>();
	}
	
	public boolean existeErros() {
		return !this.erros.isEmpty() || !this.erroSimples.isEmpty();
	}
	
	public void setError(String mensagem, Object motivo) {
		erros.put(mensagem, motivo);
	}
	
	public void setAllErrors(Map<String, Object> erros) {
		this.erros.putAll(erros);
	}
	
	public Map<String, Object> getAllErrors() {
		return this.erros;
	}

	public List<String> getErroSimples() {
		return erroSimples;
	}

	public void setErroSimples(List<String> erroSimples) {
		this.erroSimples = erroSimples;
	}

	public void addErroSimples(String erroSimples) {
		this.erroSimples.add(erroSimples);
	}
}
