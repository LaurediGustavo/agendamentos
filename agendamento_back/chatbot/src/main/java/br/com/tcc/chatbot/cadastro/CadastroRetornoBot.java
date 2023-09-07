package br.com.tcc.chatbot.cadastro;

import br.com.tcc.chatbot.RetornoChatBotInterface;
import br.com.tcc.chatbot.cadastro.enumerador.CadastroPassosEnum;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.enumerador.TipoChatBotEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uteis.Uteis;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class CadastroRetornoBot extends RetornoChatBotInterface {

    @Autowired
    private CadastroPassosFactory cadastroPassosFactory;

    @Override
    public SendMessage processarRetorno(Message message, MonitorDeChatBot monitorDeChatBot) {
        SendMessage sendMessage = null;

        if(monitorDeChatBot == null) {
            monitorDeChatBot = super.cadastrarMonitor(message, TipoChatBotEnum.CADASTRO);
        }

        sendMessage = cadastroPassosFactory.processar(getPasso(monitorDeChatBot))
                .processarPassosDeCadastro(monitorDeChatBot, message);

        return sendMessage;
    }

    private CadastroPassosEnum getPasso(MonitorDeChatBot monitorDeChatBot) {
        return Arrays.stream(CadastroPassosEnum.values())
                .filter(passo -> passo.getPASSO().equals(monitorDeChatBot.getPasso())).findFirst()
                .orElse(CadastroPassosEnum.PASSO_UM);
    }

    @Override
    public TipoChatBotEnum getTipoChatBot() {
        return TipoChatBotEnum.CADASTRO;
    }
}
