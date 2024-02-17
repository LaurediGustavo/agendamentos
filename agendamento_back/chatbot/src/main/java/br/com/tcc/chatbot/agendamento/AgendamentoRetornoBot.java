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
import java.util.List;

@Component
public class AgendamentoRetornoBot extends RetornoChatBotInterface {

    @Autowired
    private AgendamentoPassosFactory agendamentoPassosFactory;

    @Override
    public List<SendMessage> processarRetorno(Message message, MonitorDeChatBot monitorDeChatBot) {
        List<SendMessage> messages = null;

        if(monitorDeChatBot == null) {
            monitorDeChatBot = super.cadastrarMonitor(message, TipoChatBotEnum.AGENDAMENTO);
        }

        messages = agendamentoPassosFactory.processar(getPasso(monitorDeChatBot))
                .processarPassosDeAgendamento(monitorDeChatBot, message);

        return messages;
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
