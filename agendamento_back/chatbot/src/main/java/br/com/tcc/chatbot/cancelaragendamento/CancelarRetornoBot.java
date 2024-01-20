package br.com.tcc.chatbot.cancelaragendamento;

import br.com.tcc.chatbot.RetornoChatBotInterface;
import br.com.tcc.chatbot.cancelaragendamento.enumerador.CancelarPassosEnum;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.enumerador.TipoChatBotEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.List;

@Component
public class CancelarRetornoBot extends RetornoChatBotInterface {

    @Autowired
    private CancelarPassosFactory cancelarPassosFactory;

    @Override
    public List<SendMessage> processarRetorno(Message message, MonitorDeChatBot monitorDeChatBot) {
        List<SendMessage> messages = null;

        if(monitorDeChatBot == null) {
            monitorDeChatBot = super.cadastrarMonitor(message, TipoChatBotEnum.CANCELAR);
        }

        messages = cancelarPassosFactory.processar(getPasso(monitorDeChatBot))
                .processarPassosDeCadastro(monitorDeChatBot, message);

        return messages;
    }

    private CancelarPassosEnum getPasso(MonitorDeChatBot monitorDeChatBot) {
        return Arrays.stream(CancelarPassosEnum.values())
                .filter(passo -> passo.getPASSO().equals(monitorDeChatBot.getPasso())).findFirst()
                .orElse(CancelarPassosEnum.PASSO_UM);
    }

    @Override
    public TipoChatBotEnum getTipoChatBot() {
        return TipoChatBotEnum.CANCELAR;
    }
}
