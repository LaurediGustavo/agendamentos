package br.com.tcc.chatbot.remarcar;

import br.com.tcc.chatbot.RetornoChatBotInterface;
import br.com.tcc.chatbot.remarcar.enumerador.RemarcarPassosEnum;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.enumerador.TipoChatBotEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.List;

@Component
public class RemarcarRetornoBot extends RetornoChatBotInterface {

    @Autowired
    private RemarcarPassosFactory remarcarPassosFactory;

    @Override
    public List<SendMessage> processarRetorno(Message message, MonitorDeChatBot monitorDeChatBot) {
        List<SendMessage> messages = null;

        if(monitorDeChatBot == null) {
            monitorDeChatBot = super.cadastrarMonitor(message, TipoChatBotEnum.REMARCAR);
        }

        messages = remarcarPassosFactory.processar(getPasso(monitorDeChatBot))
                .processarPassosDeRemarcar(monitorDeChatBot, message);

        return messages;
    }

    private RemarcarPassosEnum getPasso(MonitorDeChatBot monitorDeChatBot) {
        return Arrays.stream(RemarcarPassosEnum.values())
                .filter(passo -> passo.getPASSO().equals(monitorDeChatBot.getPasso())).findFirst()
                .orElse(RemarcarPassosEnum.PASSO_UM);
    }

    @Override
    public TipoChatBotEnum getTipoChatBot() {
        return TipoChatBotEnum.REMARCAR;
    }
}
