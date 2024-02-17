package br.com.tcc.chatbot.sessao;

import br.com.tcc.chatbot.BotListener;
import br.com.tcc.entity.MonitorDeChatBot;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class EncerrarSessaoBot extends BotListener {

    public void enviarMensagemDeEncerramento(MonitorDeChatBot monitorDeChatBot) throws TelegramApiException {
        SendMessage message = createMensagem(monitorDeChatBot);
        execute(message);
    }

    private SendMessage createMensagem(MonitorDeChatBot monitorDeChatBot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(monitorDeChatBot.getChatId().toString());
        sendMessage.setText(montarMensagem());

        return sendMessage;
    }

    private String montarMensagem() {
        StringBuilder string = new StringBuilder();
        string.append("Você ficou muito tempo sem interagir. Sessão encerrada!");

        return string.toString();
    }
}
