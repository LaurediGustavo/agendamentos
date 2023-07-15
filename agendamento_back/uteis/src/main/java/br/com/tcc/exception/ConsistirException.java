package br.com.tcc.exception;

import java.util.HashMap;
import java.util.Map;

public class ConsistirException extends Exception {

	private static final long serialVersionUID = -7938983426568873552L;

	private Map<String, Object> erros;
	
	public ConsistirException() {
		this.erros = new HashMap<>();
	}
	
	public boolean existeErros() {
		return !this.erros.isEmpty();
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
	
}
