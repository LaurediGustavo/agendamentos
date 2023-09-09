package br.com.tcc.chatbot.agendamento.interfaces;

import br.com.tcc.chatbot.agendamento.enumerador.AgendamentoPassosEnum;
import br.com.tcc.chatbot.cadastro.enumerador.CadastroPassosEnum;
import br.com.tcc.entity.MonitorDeChatBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface AgendamentoPassosInterface {

    SendMessage processarPassosDeAgendamento(MonitorDeChatBot monitorDeChatBot, Message message);

    AgendamentoPassosEnum getPasso();

}
