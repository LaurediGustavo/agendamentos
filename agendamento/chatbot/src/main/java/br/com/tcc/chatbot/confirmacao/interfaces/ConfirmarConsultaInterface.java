package br.com.tcc.chatbot.confirmacao.interfaces;

import br.com.tcc.entity.Paciente;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface ConfirmarConsultaInterface {

	public void iniciarConversa(Paciente paciente) throws TelegramApiException;
	
}
