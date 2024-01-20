package br.com.tcc.chatbot.cancelaragendamento.interfaces;

import br.com.tcc.chatbot.cancelaragendamento.enumerador.CancelarPassosEnum;
import br.com.tcc.entity.MonitorDeChatBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface CancelarPassosInterface {

    List<SendMessage> processarPassosDeCadastro(MonitorDeChatBot monitorDeChatBot, Message message);

    CancelarPassosEnum getPasso();

}
