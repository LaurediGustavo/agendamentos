package br.com.tcc.chatbot.cancelaroperacao;

import br.com.tcc.chatbot.RetornoChatBotInterface;
import br.com.tcc.chatbot.menu.MenuChatBot;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.enumerador.TipoChatBotEnum;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CancelarOperacaoRetornoBot extends RetornoChatBotInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private MenuChatBot menuChatBot;

    @Override
    public List<SendMessage> processarRetorno(Message message, MonitorDeChatBot monitorDeChatBot) {
        List<SendMessage> messages = null;

        atualizarMonitor(monitorDeChatBot);

        messages = montarMensagem(message.getChatId());
        messages.add(menuChatBot.getMenu(message.getChatId()));

        return messages;
    }

    private List<SendMessage> montarMensagem(Long chatId) {
        String string = """ 
                        Operação cancelada com sucesso!
                        
                        """;

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(string);

        return new ArrayList<>(List.of(sendMessage));
    }

    private void atualizarMonitor(MonitorDeChatBot monitorDeChatBot) {
        monitorDeChatBot.setDataDaMensagem(LocalDateTime.now());
        monitorDeChatBot.setStatusDaMensagemChatBotEnum(StatusDaMensagemChatBotEnum.CANCELADO);
        monitorDeChatBot.setPasso(5);

        monitorDeChatBotRepository.save(monitorDeChatBot);
    }

    @Override
    public TipoChatBotEnum getTipoChatBot() {
        return TipoChatBotEnum.CANCELAR_OPERACAO;
    }
}
