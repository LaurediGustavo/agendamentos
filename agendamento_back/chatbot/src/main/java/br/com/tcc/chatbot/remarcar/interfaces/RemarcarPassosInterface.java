package br.com.tcc.chatbot.remarcar.interfaces;

import br.com.tcc.chatbot.remarcar.enumerador.RemarcarPassosEnum;
import br.com.tcc.entity.MonitorDeChatBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface RemarcarPassosInterface {

    List<SendMessage> processarPassosDeRemarcar(MonitorDeChatBot monitorDeChatBot, Message message);

    RemarcarPassosEnum getPasso();

}
