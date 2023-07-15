package br.com.tcc.chatbot.generic;

import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.entity.Paciente;
import br.com.tcc.enumerador.TipoChatBotEnum;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface RetornoChatBotInterface {

    void processarRetorno(Message message, MonitorDeChatBot monitorDeChatBot);

    TipoChatBotEnum getTipoChatBot();

}
