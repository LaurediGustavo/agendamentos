package br.com.tcc.enumerador;

public enum TipoChatBotEnum {
	
	CONFIRMACAO("0"),
	CADASTRO("1"),
	AGENDAMENTO("2"),
	REMARCAR("3"),
	CANCELAR("4"),
	CONSULTAR("5");

	private final String VALUE;

	TipoChatBotEnum(String value) {
		VALUE = value;
	}

	public String getVALUE() {
		return VALUE;
	}
}
