package br.com.tcc.chatbot.cancelaragendamento.interfaces;

import br.com.tcc.chatbot.cancelaragendamento.enumerador.CancelarPassosEnum;
import br.com.tcc.entity.MonitorDeChatBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface CancelarPassosInterface {

    SendMessage processarPassosDeCadastro(MonitorDeChatBot monitorDeChatBot, Message message);

    CancelarPassosEnum getPasso();

}
