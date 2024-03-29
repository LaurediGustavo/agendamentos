package br.com.tcc.chatbot.cadastro.interfaces;

import br.com.tcc.chatbot.cadastro.enumerador.CadastroPassosEnum;
import br.com.tcc.entity.MonitorDeChatBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface CadastroPassosInterface {

    List<SendMessage> processarPassosDeCadastro(MonitorDeChatBot monitorDeChatBot, Message message);

    CadastroPassosEnum getPasso();

}
