package br.com.tcc.chatbot.consulta;

import br.com.tcc.chatbot.RetornoChatBotInterface;
import br.com.tcc.chatbot.cadastro.enumerador.CadastroPassosEnum;
import br.com.tcc.chatbot.consulta.enumerador.ConsultaPassosEnum;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.enumerador.TipoChatBotEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;

@Component
public class ConsultaRetornoBot extends RetornoChatBotInterface {

    @Autowired
    private ConsultaPassosFactory consultaPassosFactory;

    @Override
    public SendMessage processarRetorno(Message message, MonitorDeChatBot monitorDeChatBot) {
        SendMessage sendMessage = null;

        if(monitorDeChatBot == null) {
            monitorDeChatBot = super.cadastrarMonitor(message, TipoChatBotEnum.CONSULTAR);
        }

        sendMessage = consultaPassosFactory.processar(getPasso(monitorDeChatBot))
                .processarPassosDeCadastro(monitorDeChatBot, message);

        return sendMessage;
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
