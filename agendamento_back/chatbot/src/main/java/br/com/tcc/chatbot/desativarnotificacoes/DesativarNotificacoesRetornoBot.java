package br.com.tcc.chatbot.desativarnotificacoes;

import br.com.tcc.chatbot.RetornoChatBotInterface;
import br.com.tcc.chatbot.desativarnotificacoes.enumerador.DesativarNotificacoesPassosEnum;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.enumerador.TipoChatBotEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.List;

@Component
public class DesativarNotificacoesRetornoBot extends RetornoChatBotInterface {

    @Autowired
    private DesativarNotificacoesPassosFactory desativarNotificacoesPassosFactory;

    @Override
    public List<SendMessage> processarRetorno(Message message, MonitorDeChatBot monitorDeChatBot) {
        List<SendMessage> messages = null;

        if(monitorDeChatBot == null) {
            monitorDeChatBot = super.cadastrarMonitor(message, TipoChatBotEnum.DESATIVAR_NOTIFICACAO);
        }

        messages = desativarNotificacoesPassosFactory.processar(getPasso(monitorDeChatBot))
                .processarPassosDeRemarcar(monitorDeChatBot, message);

        return messages;
    }

    private DesativarNotificacoesPassosEnum getPasso(MonitorDeChatBot monitorDeChatBot) {
        return Arrays.stream(DesativarNotificacoesPassosEnum.values())
                .filter(passo -> passo.getPASSO().equals(monitorDeChatBot.getPasso())).findFirst()
                .orElse(DesativarNotificacoesPassosEnum.PASSO_UM);
    }

    @Override
    public TipoChatBotEnum getTipoChatBot() {
        return TipoChatBotEnum.DESATIVAR_NOTIFICACAO;
    }

}
