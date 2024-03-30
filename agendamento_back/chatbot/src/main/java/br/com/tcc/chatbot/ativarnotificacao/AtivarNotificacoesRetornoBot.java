package br.com.tcc.chatbot.ativarnotificacao;

import br.com.tcc.chatbot.RetornoChatBotInterface;
import br.com.tcc.chatbot.ativarnotificacao.enumerador.AtivarNotificacoesPassosEnum;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.enumerador.TipoChatBotEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.List;

@Component
public class AtivarNotificacoesRetornoBot extends RetornoChatBotInterface {

    @Autowired
    private AtivarNotificacoesPassosFactory receberNotificacoesPassosFactory;

    @Override
    public List<SendMessage> processarRetorno(Message message, MonitorDeChatBot monitorDeChatBot) {
        List<SendMessage> messages = null;

        if(monitorDeChatBot == null) {
            monitorDeChatBot = super.cadastrarMonitor(message, TipoChatBotEnum.RECEBER_NOTIFICACAO);
        }

        messages = receberNotificacoesPassosFactory.processar(getPasso(monitorDeChatBot))
                .processarPassosDeRemarcar(monitorDeChatBot, message);

        return messages;
    }

    private AtivarNotificacoesPassosEnum getPasso(MonitorDeChatBot monitorDeChatBot) {
        return Arrays.stream(AtivarNotificacoesPassosEnum.values())
                .filter(passo -> passo.getPASSO().equals(monitorDeChatBot.getPasso())).findFirst()
                .orElse(AtivarNotificacoesPassosEnum.PASSO_UM);
    }

    @Override
    public TipoChatBotEnum getTipoChatBot() {
        return TipoChatBotEnum.RECEBER_NOTIFICACAO;
    }

}
