package br.com.tcc.chatbot.sessao;

import br.com.tcc.entity.AgendamentoChatBot;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.entity.PacienteChatBot;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EncerrarSessao {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private AgendamentoChatBotRepository agendamentoChatBotRepository;

    @Autowired
    private PacienteChatBotRepository pacienteChatBotRepository;

    @Autowired
    private CancelarAgendamentoChatBotRepository cancelarAgendamentoChatBotRepository;

    @Autowired
    private RemarcarAgendamentoChatBotRepository remarcarAgendamentoChatBotRepository;

    public void encerrarSessao(MonitorDeChatBot monitor) {
        this.encerrarMonitor(monitor);
        this.limparAcoesExistentes(monitor);
    }

    private void limparAcoesExistentes(MonitorDeChatBot monitorDeChatBot) {
        agendamentoChatBotRepository.deleteByChatId(monitorDeChatBot.getChatId());
        pacienteChatBotRepository.deleteByChatId(monitorDeChatBot.getChatId());
        cancelarAgendamentoChatBotRepository.deleteByChatId(monitorDeChatBot.getChatId());
        remarcarAgendamentoChatBotRepository.deleteByChatId(monitorDeChatBot.getChatId());
    }

    private void encerrarMonitor(MonitorDeChatBot monitor) {
        monitor.setStatusDaMensagemChatBotEnum(StatusDaMensagemChatBotEnum.CANCELADO);
        monitorDeChatBotRepository.save(monitor);
    }

}
