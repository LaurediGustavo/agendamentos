package br.com.tcc.chatbot.agendamento;

import br.com.tcc.chatbot.RetornoChatBotInterface;
import br.com.tcc.chatbot.agendamento.enumerador.AgendamentoPassosEnum;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.enumerador.TipoChatBotEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;

@Component
public class AgendamentoRetornoBot extends RetornoChatBotInterface {

    @Autowired
    private AgendamentoPassosFactory agendamentoPassosFactory;

    @Override
    public SendMessage processarRetorno(Message message, MonitorDeChatBot monitorDeChatBot) {
        SendMessage sendMessage = null;

        if(monitorDeChatBot == null) {
            monitorDeChatBot = super.cadastrarMonitor(message, TipoChatBotEnum.AGENDAMENTO);
        }

        sendMessage = agendamentoPassosFactory.processar(getPasso(monitorDeChatBot))
                .processarPassosDeAgendamento(monitorDeChatBot, message);

        return sendMessage;
    }

    private AgendamentoPassosEnum getPasso(MonitorDeChatBot monitorDeChatBot) {
        return Arrays.stream(AgendamentoPassosEnum.values())
                .filter(passo -> passo.getPASSO().equals(monitorDeChatBot.getPasso())).findFirst()
                .orElse(AgendamentoPassosEnum.PASSO_UM);
    }

    @Override
    public TipoChatBotEnum getTipoChatBot() {
        return TipoChatBotEnum.AGENDAMENTO;
    }
}
