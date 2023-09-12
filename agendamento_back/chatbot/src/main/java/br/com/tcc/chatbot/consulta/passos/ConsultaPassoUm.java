package br.com.tcc.chatbot.consulta.passos;

import br.com.tcc.chatbot.consulta.enumerador.ConsultaPassosEnum;
import br.com.tcc.chatbot.consulta.interfaces.ConsultaPassosInterface;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;

@Component
public class ConsultaPassoUm implements ConsultaPassosInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Override
    public SendMessage processarPassosDeCadastro(MonitorDeChatBot monitorDeChatBot, Message message) {
        atualizarMonitor(monitorDeChatBot);
        return montarMensagem(message.getChatId(), "Por favor informe o seu CPF");
    }

    private void atualizarMonitor(MonitorDeChatBot monitorDeChatBot) {
        monitorDeChatBot.setDataDaMensagem(LocalDateTime.now());
        monitorDeChatBot.setPasso(2);

        monitorDeChatBotRepository.save(monitorDeChatBot);
    }

    private SendMessage montarMensagem(Long chatId, String mensagem) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(mensagem);

        return sendMessage;
    }

    @Override
    public ConsultaPassosEnum getPasso() {
        return ConsultaPassosEnum.PASSO_UM;
    }
}
