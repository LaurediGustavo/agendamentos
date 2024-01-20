package br.com.tcc.chatbot.consulta;

import br.com.tcc.chatbot.RetornoChatBotInterface;
import br.com.tcc.chatbot.consulta.enumerador.ConsultaPassosEnum;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.enumerador.TipoChatBotEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.List;

@Component
public class ConsultaRetornoBot extends RetornoChatBotInterface {

    @Autowired
    private ConsultaPassosFactory consultaPassosFactory;

    @Override
    public List<SendMessage> processarRetorno(Message message, MonitorDeChatBot monitorDeChatBot) {
        List<SendMessage> messages = null;

        if(monitorDeChatBot == null) {
            monitorDeChatBot = super.cadastrarMonitor(message, TipoChatBotEnum.CONSULTAR);
        }

        messages = consultaPassosFactory.processar(getPasso(monitorDeChatBot))
                .processarPassosDeCadastro(monitorDeChatBot, message);

        return messages;
    }

    private ConsultaPassosEnum getPasso(MonitorDeChatBot monitorDeChatBot) {
        return Arrays.stream(ConsultaPassosEnum.values())
                .filter(passo -> passo.getPASSO().equals(monitorDeChatBot.getPasso())).findFirst()
                .orElse(ConsultaPassosEnum.PASSO_UM);
    }

    @Override
    public TipoChatBotEnum getTipoChatBot() {
        return TipoChatBotEnum.CONSULTAR;
    }
}
