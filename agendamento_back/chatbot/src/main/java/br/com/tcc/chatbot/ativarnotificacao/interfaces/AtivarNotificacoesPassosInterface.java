package br.com.tcc.chatbot.ativarnotificacao.interfaces;

import br.com.tcc.chatbot.ativarnotificacao.enumerador.AtivarNotificacoesPassosEnum;
import br.com.tcc.entity.MonitorDeChatBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface AtivarNotificacoesPassosInterface {

    List<SendMessage> processarPassosDeRemarcar(MonitorDeChatBot monitorDeChatBot, Message message);

    AtivarNotificacoesPassosEnum getPasso();

}
