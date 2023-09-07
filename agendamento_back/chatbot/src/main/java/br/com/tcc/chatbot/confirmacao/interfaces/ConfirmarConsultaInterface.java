package br.com.tcc.chatbot.confirmacao.interfaces;

import br.com.tcc.entity.Consulta;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface ConfirmarConsultaInterface {

	public void iniciarConversa(Consulta consulta) throws TelegramApiException;
	
}
