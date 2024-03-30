package br.com.tcc.chatbot.desativarnotificacoes.passos;

import br.com.tcc.chatbot.desativarnotificacoes.enumerador.DesativarNotificacoesPassosEnum;
import br.com.tcc.chatbot.desativarnotificacoes.interfaces.DesativarNotificacoesPassosInterface;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DesativarNotificacoesPassoUm implements DesativarNotificacoesPassosInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Override
    public List<SendMessage> processarPassosDeRemarcar(MonitorDeChatBot monitorDeChatBot, Message message) {
        atualizarMonitor(monitorDeChatBot);
        return montarMensagem(message.getChatId(), "Para você não receber mais notificações, nos informe seu CPF.");
    }

    private void atualizarMonitor(MonitorDeChatBot monitorDeChatBot) {
        monitorDeChatBot.setDataDaMensagem(LocalDateTime.now());
        monitorDeChatBot.setPasso(2);

        monitorDeChatBotRepository.save(monitorDeChatBot);
    }

    private List<SendMessage> montarMensagem(Long chatId, String mensagem) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(mensagem);

        return new ArrayList<>(List.of(sendMessage));
    }

    @Override
    public DesativarNotificacoesPassosEnum getPasso() {
        return DesativarNotificacoesPassosEnum.PASSO_UM;
    }

}
