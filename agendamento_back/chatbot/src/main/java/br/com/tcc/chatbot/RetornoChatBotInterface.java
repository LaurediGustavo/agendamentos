package br.com.tcc.chatbot;

import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.entity.Paciente;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.enumerador.TipoChatBotEnum;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;

@Component("RetornoChatBotInterface")
public abstract class RetornoChatBotInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    public abstract SendMessage processarRetorno(Message message, MonitorDeChatBot monitorDeChatBot);

    public abstract TipoChatBotEnum getTipoChatBot();

    public MonitorDeChatBot cadastrarMonitor(Message message, TipoChatBotEnum tipoChatBotEnum) {
        MonitorDeChatBot monitor = new MonitorDeChatBot();
        monitor.setDataDaMensagem(LocalDateTime.now());
        monitor.setTipoChatBotEnum(tipoChatBotEnum);
        monitor.setStatusDaMensagemChatBotEnum(StatusDaMensagemChatBotEnum.AGUARDANDO);
        monitor.setChatId(message.getChatId());
        monitor.setPasso(1);

        return monitorDeChatBotRepository.save(monitor);
    }

}
