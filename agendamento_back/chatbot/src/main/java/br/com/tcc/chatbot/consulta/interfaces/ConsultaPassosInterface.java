package br.com.tcc.chatbot.consulta.interfaces;

import br.com.tcc.chatbot.consulta.enumerador.ConsultaPassosEnum;
import br.com.tcc.entity.MonitorDeChatBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface ConsultaPassosInterface {

    List<SendMessage> processarPassosDeCadastro(MonitorDeChatBot monitorDeChatBot, Message message);

    ConsultaPassosEnum getPasso();

}
