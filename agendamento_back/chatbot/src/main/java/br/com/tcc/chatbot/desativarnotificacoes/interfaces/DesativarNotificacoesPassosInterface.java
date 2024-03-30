package br.com.tcc.chatbot.desativarnotificacoes.interfaces;

import br.com.tcc.chatbot.desativarnotificacoes.enumerador.DesativarNotificacoesPassosEnum;
import br.com.tcc.entity.MonitorDeChatBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface DesativarNotificacoesPassosInterface {

    List<SendMessage> processarPassosDeRemarcar(MonitorDeChatBot monitorDeChatBot, Message message);

    DesativarNotificacoesPassosEnum getPasso();

}
