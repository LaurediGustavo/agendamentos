package br.com.tcc.chatbot.confirmacao.impl;

import br.com.tcc.chatbot.generic.RetornoChatBotInterface;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.enumerador.TipoChatBotEnum;
import br.com.tcc.repository.ConsultaRepository;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ConfirmarConsultaRetornoBot implements RetornoChatBotInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    public void processarRetorno(Message message, MonitorDeChatBot monitorDeChatBot) {
        String messageText = message.getText().toUpperCase();

        if ("SIM".equals(messageText)) {
            System.out.println(messageText);
        }
        else {
            System.out.println(messageText);
        }

        monitorDeChatBot.setStatusDaMensagemChatBotEnum(StatusDaMensagemChatBotEnum.FINALIZADO);
        monitorDeChatBotRepository.save(monitorDeChatBot);
    }

    @Override
    public TipoChatBotEnum getTipoChatBot() {
        return TipoChatBotEnum.CONFIRMACAO;
    }
}
